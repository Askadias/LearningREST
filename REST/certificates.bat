keytool -genkey -alias cryptoservice -keyalg RSA -keypass Pa55w0rd4CryprtoService -validity 3650 -dname "CN=localhost, O=forxy, C=RU" -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
keytool -genkey -alias cryptoclient -keyalg RSA -keypass MyCrypt0ClientPa55w0rd -validity 3650 -dname "CN=localhost, O=forxy, C=RU" -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS

keytool -export -alias cryptoclient -keyalg RSA -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS -file cryptoClient.cer
keytool -import -alias cryptoclient -keyalg RSA -file cryptoClient.cer -noprompt -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS

keytool -export -alias cryptoservice -keyalg RSA -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -storetype JKS -file cryptoService.cer
keytool -import -alias cryptoservice -keyalg RSA -file cryptoService.cer -noprompt -keystore cryptoClientTrustStore.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS