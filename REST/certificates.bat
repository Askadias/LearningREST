keytool -genkey -alias cryptoservice -keyalg RSA -keypass cerv1ce3ncrypt10n -validity 3650 -dname "CN=localhost, O=forxy, C=RU" -keystore cryptoServer.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
keytool -genkey -alias cryptoclient -keyalg RSA -keypass c1ient3ncrypt10n -validity 3650 -dname "CN=localhost, O=forxy, C=RU" -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS
keytool -export -alias cryptoclient -keyalg RSA -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS -file cryptoClient.cer
keytool -import -alias cryptoclient -keyalg RSA -file cryptoClient.cer -noprompt -keystore cryptoServiceTrustStore.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
keytool -import -alias cryptoclient -keyalg RSA -file cryptoClient.cer -noprompt -keystore cryptoServer.jks -storepass Pa55w0rd4CryprtoService -storetype JKS
keytool -export -alias cryptoservice -keyalg RSA -keystore cryptoServer.jks -storepass Pa55w0rd4CryprtoService -storetype JKS -file cryptoService.cer
keytool -import -alias cryptoservice -keyalg RSA -file cryptoClient.cer -noprompt -keystore cryptoClientTrustStore.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS
keytool -import -alias cryptoservice -keyalg RSA -file cryptoClient.cer -noprompt -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -storetype JKS