keytool -genkey -v -alias cryptoservice -keyalg RSA -keypass Pa55w0rd4CryprtoService -validity 3650 -dname "CN=localhost, OU=CryptoService, O=Forxy, C=BY" -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
keytool -genkey -v -alias cryptoclient -keyalg RSA -keypass MyCrypt0ClientPa55w0rd -validity 3650 -dname "CN=Crypto Client, OU=CryptoClient, O=Forxy, C=BY" -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS
keytool -genkey -v -alias cryptoclientweb -keyalg RSA -keypass MyCrypt0ClientPa55w0rd -validity 3650 -dname "CN=Crypto Client Web, OU=CryptoClientWeb, O=Forxy, C=BY" -keystore cryptoClientWeb.p12 -storepass MyCrypt0ClientPa55w0rd -storetype PKCS12

keytool -export -rfc -alias cryptoclientweb -keyalg RSA -keystore cryptoClientWeb.p12 -storepass MyCrypt0ClientPa55w0rd -storetype PKCS12 -file cryptoClientWeb.cer
keytool -import -v -noprompt -alias cryptoclientweb -keyalg RSA -file cryptoClientWeb.cer -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS

keytool -export -rfc -alias cryptoclient -keyalg RSA -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS -file cryptoClient.cer
keytool -import -v -noprompt -alias cryptoclient -keyalg RSA -file cryptoClient.cer -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS

keytool -export -rfc -alias cryptoservice -keyalg RSA -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS -file cryptoService.cer
keytool -import -v -noprompt -alias cryptoservice -keyalg RSA -file cryptoService.cer -keystore cryptoClientTrustStore.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS


keytool -genkey -v -alias web -keyalg RSA -keypass My5ecredWebPa55w0rd -validity 3650 -dname "CN=localhost, OU=Web, O=Forxy, C=BY" -keystore web.jks -storepass My5ecredWebPa55w0rd -storetype JKS