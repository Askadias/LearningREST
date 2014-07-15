:: ---------------------- Crypto Service and Client Certificates ----------------------------------------
:: create server key store
keytool -genkey -v -alias cryptoservice -keyalg RSA -keypass Pa55w0rd4CryprtoService -validity 3650 -dname "CN=localhost, OU=CryptoService, O=Forxy, C=BY" -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
:: create client 1 key store
keytool -genkey -v -alias cryptoclient -keyalg RSA -keypass MyCrypt0ClientPa55w0rd -validity 3650 -dname "CN=Crypto Client, OU=CryptoClient, O=Forxy, C=BY" -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS
:: create client 2 key store
keytool -genkey -v -alias cryptoclientweb -keyalg RSA -keypass MyCrypt0ClientPa55w0rd -validity 3650 -dname "CN=Crypto Client Web, OU=CryptoClientWeb, O=Forxy, C=BY" -keystore cryptoClientWeb.p12 -storepass MyCrypt0ClientPa55w0rd -storetype PKCS12

:: export client 2 certificate
keytool -export -rfc -alias cryptoclientweb -keyalg RSA -keystore cryptoClientWeb.p12 -storepass MyCrypt0ClientPa55w0rd -storetype PKCS12 -file cryptoClientWeb.cer
:: import client 2 certificate into server key store
keytool -import -v -noprompt -alias cryptoclientweb -keyalg RSA -file cryptoClientWeb.cer -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS

:: export client 1 certificate
keytool -export -rfc -alias cryptoclient -keyalg RSA -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS -file cryptoClient.cer
:: import client 1 certificate into server key store
keytool -import -v -noprompt -alias cryptoclient -keyalg RSA -file cryptoClient.cer -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS

:: export server certificate
keytool -export -rfc -alias cryptoservice -keyalg RSA -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS -file cryptoService.cer
:: import server certificate into clients trust store
keytool -import -v -noprompt -alias cryptoservice -keyalg RSA -file cryptoService.cer -keystore cryptoClientTrustStore.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS

:: ---------------------- Web Application Certificates --------------------------------------------------
:: create self signed public store
keytool -genkey -v -alias web -keyalg RSA -keypass My5ecredWebPa55w0rd -validity 3650 -dname "CN=localhost, OU=Web, O=Forxy, C=BY" -keystore web.jks -storepass My5ecredWebPa55w0rd -storetype JKS

:: export web server certificate
keytool -export -rfc -alias web -keyalg RSA -keystore web.jks -storepass My5ecredWebPa55w0rd -storetype JKS -file web.cer
:: import web server certificate into clients trust store
keytool -import -v -noprompt -alias web -keyalg RSA -file web.cer -keystore webTrustStore.jks -storepass My5ecredWebPa55w0rd -storetype JKS

:: ---------------------- OAUTH Certificates ------------------------------------------------------------
:: create self signed public store
keytool -genkey -v -alias oauth -keyalg RSA -keypass 5ecret0AUTHPa55word -validity 3650 -dname "CN=localhost, OU=OAuth, O=Forxy, C=BY" -keystore oauth.jks -storepass 5ecret0AUTHPa55word -storetype JKS

:: export oauth server certificate
keytool -export -rfc -alias oauth -keyalg RSA -keystore oauth.jks -storepass 5ecret0AUTHPa55word -storetype JKS -file oauth.cer
:: import oauth server certificate into clients trust store
keytool -import -v -noprompt -alias oauth -keyalg RSA -file oauth.cer -keystore oauthTrustStore.jks -storepass 5ecret0AUTHPa55word -storetype JKS