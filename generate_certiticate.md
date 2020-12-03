Certificate 

1> create a file name req.cnf with below content
```
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no
[req_distinguished_name]
C = IN
ST = KN
L = BLR
O = PICHUKI
OU = POD
CN = *.amazonaws.com
[v3_req]
basicConstraints = CA:FALSE
keyUsage = nonRepudiation, digitalSignature, keyEncipherment
extendedKeyUsage = serverAuth, clientAuth
subjectAltName = @alt_names
[alt_names]
DNS.1=*.amazonaws.com
DNS.2=s3.us-west-2.amazonaws.com
DNS.3=*.s3.us-west-2.amazonaws.com
DNS.4=s3-us-west-2.amazonaws.com
DNS.5=*.s3-us-west-2.amazonaws.com
DNS.6=ec2.us-west-2.amazonaws.com
DNS.7=cloudformation.us-west-2.amazonaws.com
DNS.8=*.us-west-2.amazonaws.com
DNS.9=iam.amazonaws.com
DNS.10=sts.amazonaws.com
IP.1=127.0.0.1
```
2>openssl genrsa -out moto_server.key 2048

3>openssl req -sha256 -new -key moto_server.key -out moto_server.csr -config req.cnf

4>openssl x509 -req -days 3650 -in moto_server.csr -out moto_server.crt -CA cacert.pem -CAkey cakey.pem -extensions v3_req -CAcreateserial -extfile req.cnf
Note: cacert.pem and cacert.key is existing ca certificate present in moto

5>openssl x509 -in moto_server.crt -text


keytool -import -trustcacerts -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home/jre/lib/security/cacerts -noprompt -alias moto_server -file cacert.pem
