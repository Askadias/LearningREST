keytool -genkeypair -alias cryptoservice -keystore cryptoService.jks -keypass Pa55w0rd4CryprtoService -storepass Pa55w0rd4CryprtoService -dname "CN=localhost, O=forxy, C=RU" -validity 3650
keytool -genkeypair -alias cryptoclient -keystore cryptoClient.jks -keypass MyCrypt0ClientPa55w0rd -storepass MyCrypt0ClientPa55w0rd -dname "CN=localhost, O=forxy, C=RU" -validity 3650

keytool -export -rfc -alias cryptoservice -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -file cryptoServer.cer
keytool -import -noprompt -trustcacerts -alias cryptoservice -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -file cryptoServer.cer

keytool -export -rfc -alias cryptoclient -keystore cryptoClient.jks -storepass MyCrypt0ClientPa55w0rd -file cryptoClient.cer
keytool -import -noprompt -trustcacerts -alias cryptoclient -keystore cryptoService.jks -storepass Pa55w0rd4CryprtoService -file cryptoClient.cer