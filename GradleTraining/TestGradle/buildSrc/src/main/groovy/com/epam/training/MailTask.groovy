package com.epam.training

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailTask extends DefaultTask {

    String to
    String subject
    String content
    String from
    String password

    @TaskAction
    void sendMail() {
        String host = 'smtp.gmail.com';
        Properties props = System.getProperties();
        props.put('mail.smtp.starttls.enable', true);
        props.setProperty('mail.smtp.ssl.trust', host);
        props.put('mail.smtp.auth', true);
        props.put('mail.smtp.host', host);
        props.put('mail.smtp.user', from);
        props.put('mail.smtp.password', password);
        props.put('mail.smtp.port', '587');

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        InternetAddress toAddress = new InternetAddress(to);

        message.addRecipient(Message.RecipientType.TO, toAddress);

        message.setSubject(subject);
        message.setText(content);

        Transport transport = session.getTransport('smtp');

        transport.connect(host, from, password);

        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}