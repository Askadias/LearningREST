package ru.forxy.common.logging.log4j;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.AbsoluteTimeDateFormat;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;
import ru.forxy.common.logging.Fields;
import ru.forxy.common.utils.Context;
import ru.forxy.common.utils.EncodingHelper;
import ru.forxy.common.utils.FormatHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Log4j PatternLayout able to print fields from execution context
 */
public class ContextPatternLayout extends PatternLayout {
    private static class ContextPatternConverter extends PatternConverter {
        private static final Set<String> KEYS_PAYLOAD = new HashSet<String>();
        private static final Set<String> KEYS_HTTP = new HashSet<String>();

        static {
            KEYS_PAYLOAD.add(Fields.RequestPayload.name());
            KEYS_PAYLOAD.add(Fields.ResponsePayload.name());
            KEYS_HTTP.add(Fields.RequestURL.name());
            KEYS_HTTP.add(Fields.RequestHeaders.name());
            KEYS_HTTP.add(Fields.ResponseURL.name());
            KEYS_HTTP.add(Fields.ResponseHeaders.name());
        }

        private String m_key;
        private String m_format;
        private String m_scope;

        public ContextPatternConverter(final FormattingInfo fi, final String options) {
            super(fi);
            if (options != null) {
                final String[] optionsArray = options.split(":");
                m_key = optionsArray[0];
                if (optionsArray.length > 1) {
                    m_format = optionsArray[1];
                    if (optionsArray.length > 2) {
                        m_scope = optionsArray[2];
                    }
                }
            }
        }

        /**
         * Applies conversion to %P...
         * <p/>
         * Context is either taken from logging event directly
         * (framework calls e.g Log4jWriter's m_logger.info(frame) ),
         * or from current thread context (Context.peek()) this allows using ContextPatternLayout
         * features in arbitrage loggers to add Context object into logs,
         * e.g. to add ActivityGUID to any of the application logs to correlate with perflog.
         *
         * @param event logging event
         * @return formatted string
         */
        @Override
        protected String convert(final LoggingEvent event) {
            final String result;
            final Object msg = event.getMessage();
            final Context.ContextData data = msg instanceof Context.ContextData
                    ? (Context.ContextData) msg
                    : Context.peek();
            if (m_key == null) {
                result = convertAll(data);
            } else {
                if ("stack".equals(m_key)) {
                    final StringBuilder sb = new StringBuilder(8);
                    for (int i = 0; i < data.getFrameStackSize(); i++) {
                        sb.append("..");
                    }
                    result = sb.toString();
                } else {
                    final Object value = getValue(m_key, m_scope, data);
                    result = convertSingle(value, m_format);
                }
            }
            return result;
        }

        private String convertAll(final Context.ContextData data) {
            final StringBuilder sb = new StringBuilder(1024);
            final StringBuilder ht = new StringBuilder(1024);
            final StringBuilder pl = new StringBuilder(4096);
            for (final Map.Entry<String, Object> kv : data.getGlobal().entrySet()) {
                sb.append(kv.getKey()).append("=").append(convertSingle(kv.getValue(), null)).append(" ");
            }
            if (data.getFrameStackSize() > 0) {
                for (final Map.Entry<String, Object> kv : data.getFrame().entrySet()) {
                    final String key = kv.getKey();
                    if (KEYS_HTTP.contains(key)) {
                        ht.append("\n").append("@").append(key).append("=").append(convertSingle(kv.getValue(), null));
                    } else if (KEYS_PAYLOAD.contains(key)) {
                        pl.append("\n").append("@").append(key).append("=").append(convertSingle(kv.getValue(), null));
                    } else {
                        sb.append(key).append("=").append(convertSingle(kv.getValue(), null)).append(" ");
                    }
                }
            }
            return sb.append(ht).append(pl).toString();
        }

        private String convertSingle(final Object value, final String format) {
            final String result;
            if (value != null) {
                if (value instanceof Date) {
                    result = convertDate((Date) value, format);
                } else if (value instanceof byte[]) {
                    result = convertBytes((byte[]) value, format);
                } else {
                    result = value.toString();
                }
            } else {
                result = "";
            }
            return result;
        }

        private String convertDate(final Date date, final String format) {
            DateFormat df;
            final String formatName = format != null && !"".equals(format) ? format :
                    AbsoluteTimeDateFormat.ISO8601_DATE_FORMAT;
            if (formatName.equalsIgnoreCase(AbsoluteTimeDateFormat.ISO8601_DATE_FORMAT)) {
                df = new ISO8601DateFormat();
            } else if (formatName.equalsIgnoreCase(AbsoluteTimeDateFormat.ABS_TIME_DATE_FORMAT)) {
                df = new AbsoluteTimeDateFormat();
            } else if (formatName.equalsIgnoreCase(AbsoluteTimeDateFormat.DATE_AND_TIME_DATE_FORMAT)) {
                df = new DateTimeDateFormat();
            } else {
                try {
                    df = new SimpleDateFormat(formatName, Locale.US); //hi pmd
                } catch (IllegalArgumentException ignored) {
                    df = new ISO8601DateFormat();
                }
            }
            return df.format(date);
        }

        private String convertBytes(final byte[] bytes, final String format) {
            String result = EncodingHelper.toUTFString(bytes);
            if ("indent".equalsIgnoreCase(format)) {
                result = FormatHelper.prettyFormat(result);
            } else if (format == null) {
                result = FormatHelper.prettyBreak(FormatHelper.compactFormat(result), 9900, "\n");
            }
            return result;
        }

        private Object getValue(final String key, final String scope, final Context.ContextData data) {
            Object result = null;
            if ("global".equalsIgnoreCase(scope)) {
                result = data.getGlobal().get(key);
            } else if ("frame".equalsIgnoreCase(scope) && data.getFrameStackSize() > 0) {
                result = data.getFrame().get(key);
            } else {
                if (data.getFrameStackSize() > 0) {
                    result = data.getFrame().get(key);
                }
                if (result == null) {
                    result = data.getGlobal().get(key);
                }
            }
            return result;
        }
    }

    @Override
    protected PatternParser createPatternParser(final String pattern) {
        return new PatternParser(pattern) {
            @Override
            protected void finalizeConverter(final char c) {
                PatternConverter pc = null;
                switch (c) {
                    case 'P':
                        pc = new ContextPatternConverter(formattingInfo, extractOption());
                        currentLiteral.setLength(0);
                        break;
                    default:
                        break;
                }
                if (pc == null) {
                    super.finalizeConverter(c);
                } else {
                    addConverter(pc);
                }
            }
        };
    }
}