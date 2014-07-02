import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.jasypt.spring3.properties.EncryptablePropertyPlaceholderConfigurer

beans {

    def String configDir = System.getProperty('config.dir')
    def String env = System.getProperty('env')

    encryptor(StandardPBEStringEncryptor) {
        config = { SimpleStringPBEConfig config ->
            algorithm = 'PBEWithMD5AndDES'
            password = 'secret'
            providerClassName = 'org.bouncycastle.jce.provider.BouncyCastleProvider'
        }
    }

    placeholderConfigurer(EncryptablePropertyPlaceholderConfigurer, ref(encryptor)) {
        ignoreResourceNotFound = false
        nullValue = '{null}'
        locations = [
                "file:$configDir/UserService/appconfig/base/ru.forxy.user.properties".toString(),
                "file:$configDir/UserService/appconfig/env/$env/ru.forxy.user.properties".toString()
        ]
    }

    loadBeans('classpath:spring/*.context.groovy')
}
