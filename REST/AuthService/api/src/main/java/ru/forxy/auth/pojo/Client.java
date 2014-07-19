package ru.forxy.auth.pojo;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.forxy.common.pojo.SimpleJacksonDateDeserializer;
import ru.forxy.common.pojo.SimpleJacksonDateSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class Client implements Serializable {

    private static final long serialVersionUID = -6795472289012239081L;

    @Id
    private String clientID;
    private String clientSecret;

    @Indexed(unique = true)
    private String applicationName;
    private String applicationDescription;
    private String applicationWebUri;
    private List<String> redirectUris = new ArrayList<>();
    private List<String> allowedGrantTypes = new ArrayList<>();
    private List<String> registeredScopes = new ArrayList<>();
    private List<String> registeredAudiences = new ArrayList<>();

    private Map<String, String> properties = new HashMap<>();
    private User subject;

    private Date updateDate = new Date();

    private String updatedBy;

    private Date createDate = new Date();

    private String createdBy;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getApplicationWebUri() {
        return applicationWebUri;
    }

    public void setApplicationWebUri(String applicationWebUri) {
        this.applicationWebUri = applicationWebUri;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public List<String> getAllowedGrantTypes() {
        return allowedGrantTypes;
    }

    public void setAllowedGrantTypes(List<String> allowedGrantTypes) {
        this.allowedGrantTypes = allowedGrantTypes;
    }

    public List<String> getRegisteredScopes() {
        return registeredScopes;
    }

    public void setRegisteredScopes(List<String> registeredScopes) {
        this.registeredScopes = registeredScopes;
    }

    public List<String> getRegisteredAudiences() {
        return registeredAudiences;
    }

    public void setRegisteredAudiences(List<String> registeredAudiences) {
        this.registeredAudiences = registeredAudiences;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public User getSubject() {
        return subject;
    }

    public void setSubject(User subject) {
        this.subject = subject;
    }

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    public Date getUpdateDate() {
        return updateDate;
    }

    @JsonDeserialize(using = SimpleJacksonDateDeserializer.class)
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    public Date getCreateDate() {
        return createDate;
    }

    @JsonDeserialize(using = SimpleJacksonDateDeserializer.class)
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (clientID == null) {
            if (other.clientID != null) {
                return false;
            }
        } else if (!clientID.equals(other.clientID)) {
            return false;
        }
        if (clientSecret == null) {
            if (other.clientSecret != null) {
                return false;
            }
        } else if (!clientSecret.equals(other.clientSecret)) {
            return false;
        }
        if (applicationName == null) {
            if (other.applicationName != null) {
                return false;
            }
        } else if (!applicationName.equals(other.applicationName)) {
            return false;
        }
        if (applicationDescription == null) {
            if (other.applicationDescription != null) {
                return false;
            }
        } else if (!applicationDescription.equals(other.applicationDescription)) {
            return false;
        }
        if (applicationWebUri == null) {
            if (other.applicationWebUri != null) {
                return false;
            }
        } else if (!applicationWebUri.equals(other.applicationWebUri)) {
            return false;
        }
        if (redirectUris == null) {
            if (other.redirectUris != null) {
                return false;
            }
        } else if (!redirectUris.equals(other.redirectUris)) {
            return false;
        }
        if (allowedGrantTypes == null) {
            if (other.allowedGrantTypes != null) {
                return false;
            }
        } else if (!allowedGrantTypes.equals(other.allowedGrantTypes)) {
            return false;
        }
        if (registeredScopes == null) {
            if (other.registeredScopes != null) {
                return false;
            }
        } else if (!registeredScopes.equals(other.registeredScopes)) {
            return false;
        }
        if (registeredAudiences == null) {
            if (other.registeredAudiences != null) {
                return false;
            }
        } else if (!registeredAudiences.equals(other.registeredAudiences)) {
            return false;
        }
        if (properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!properties.equals(other.properties)) {
            return false;
        }
        /*if (subject == null) {
            if (other.subject != null) {
                return false;
            }
        } else if (!subject.equals(other.subject)) {
            return false;
        }*/
        if (updateDate == null) {
            if (other.updateDate != null) {
                return false;
            }
        } else if (!updateDate.equals(other.updateDate)) {
            return false;
        }
        if (updatedBy == null) {
            if (other.updatedBy != null) {
                return false;
            }
        } else if (!updatedBy.equals(other.updatedBy)) {
            return false;
        }
        if (createDate == null) {
            if (other.createDate != null) {
                return false;
            }
        } else if (!createDate.equals(other.createDate)) {
            return false;
        }
        if (createdBy == null) {
            if (other.createdBy != null) {
                return false;
            }
        } else if (!createdBy.equals(other.createdBy)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clientID == null) ? 0 : clientID.hashCode());
        result = prime * result + ((clientSecret == null) ? 0 : clientSecret.hashCode());
        result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
        result = prime * result + ((applicationDescription == null) ? 0 : applicationDescription.hashCode());
        result = prime * result + ((applicationWebUri == null) ? 0 : applicationWebUri.hashCode());
        result = prime * result + ((redirectUris == null) ? 0 : redirectUris.hashCode());
        result = prime * result + ((allowedGrantTypes == null) ? 0 : allowedGrantTypes.hashCode());
        result = prime * result + ((registeredScopes == null) ? 0 : registeredScopes.hashCode());
        result = prime * result + ((registeredAudiences == null) ? 0 : registeredAudiences.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("{clientID=%s, applicationName=%s, applicationDescription=%s, applicationWebUri=%s, " +
                        "updateDate=%s, updatedBy=%s, createDate=%s, createdBy=%s, redirectUris=%s, " +
                        "allowedGrantTypes=%s, registeredScopes=%s, registeredAudiences=%s, properties=%s, subject=%s}",
                clientID, applicationName, applicationDescription, applicationWebUri, updateDate, updatedBy, createDate,
                createdBy, redirectUris, allowedGrantTypes, registeredScopes, registeredAudiences, properties, subject);
    }
}
