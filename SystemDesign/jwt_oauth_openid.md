 
# Table of Contents
- [Common Authentication & Authorization Protocols](#common-authentication--authorization-protocols)
- [JWT](#jwt)
- [OAuth 2.0](#oauth-20)
- [OpenID Connect](#openid-connect)
- [SAML](#saml)
- [LDAP](#ldap)

# Common Authentication & Authorization Protocols
- **OAuth 2.0**: An open standard for access delegation, allowing third-party applications to obtain limited access to user accounts on an HTTP service.
- **OpenID Connect**: An identity layer on top of OAuth 2.0, used for user authentication and single sign-on (SSO).
- **SAML (Security Assertion Markup Language)**: An XML-based framework for exchanging authentication and authorization data between parties, particularly between an identity provider and a service provider.
- **LDAP (Lightweight Directory Access Protocol)**: A protocol for accessing and maintaining distributed directory information services.
- **Kerberos**: A network authentication protocol that uses secret-key cryptography to authenticate clients to services.
- **JWT (JSON Web Token)**: A compact, URL-safe means of representing claims to be transferred between two parties, commonly used for token-based authentication.
- **RADIUS (Remote Authentication Dial-In User Service)**: A networking protocol that provides centralized authentication, authorization, and accounting for users who connect and use a network service.
- **TACACS+ (Terminal Access Controller Access-Control System Plus)**: A protocol that provides centralized authentication and authorization services for network devices.

## JWT
1. **What is JWT?**
   - JSON Web Token (JWT) is a compact, URL-safe token format used for securely transmitting information between parties as a JSON object. It is commonly used for authentication and authorization in web applications.
2. **Structure of JWT:**
   - A JWT consists of three parts: Header, Payload, and Signature, separated by dots (.)
     - **Header**: Contains metadata about the token, including the type of token (JWT) and the signing algorithm (e.g., HMAC SHA256).
     - **Payload**: Contains the claims or statements about an entity (typically, the user) and additional data. Common claims include `iss` (issuer), `sub` (subject), `exp` (expiration time), and custom claims.
     - **Signature**: Used to verify the token's integrity and authenticity. It is created by signing the header and payload with a secret key or a private key.
3. **Use Case: User Authentication**
   - **Step 1**: User logs in with their credentials (username and password).
   - **Step 2**: The server verifies the credentials. If valid, it generates a JWT containing user information and claims.
   - **Step 3**: The server sends the JWT back to the client (usually in the response body or as a cookie).
   - **Step 4**: The client stores the JWT (e.g., in local storage or a cookie) and includes it in the Authorization header of subsequent requests to protected resources. 
    - **Step 5**: The server receives the request, extracts the JWT from the Authorization header, and verifies its signature and claims (e.g., checks expiration).
    - **Step 6**: If the JWT is valid, the server processes the request and returns the requested resource. If invalid, it returns an unauthorized error.

    ## OAuth 2.0
1. **What is OAuth 2.0?**
   - OAuth 2.0 is an open standard for access delegation, allowing third-party applications to obtain limited access to user accounts on an HTTP service without exposing user credentials.
2. **Key Roles in OAuth 2.0:**
   - **Resource Owner**: The user who owns the data and authorizes access to it.
   - **Client**: The application requesting access to the resource on behalf of the resource owner.
   - **Authorization Server**: The server that issues access tokens to the client after successfully authenticating the resource owner and obtaining authorization.
   - **Resource Server**: The server hosting the protected resources, which accepts and validates access tokens.
3. **OAuth 2.0 Flow: Authorization Code Grant**
   - **Step 1**: The client redirects the resource owner to the authorization server's authorization endpoint, requesting authorization.
   - **Step 2**: The resource owner authenticates and grants authorization to the client.
   - **Step 3**: The authorization server redirects the resource owner back to the client with an authorization code.
    - **Step 4**: The client exchanges the authorization code for an access token by making a request to the authorization server's token endpoint, including its client credentials.
    - **Step 5**: The authorization server validates the authorization code and client credentials, then issues an access token (and optionally a refresh token) to the client.
    - **Step 6**: The client uses the access token to make requests to the resource server to access protected resources on behalf of the resource owner.
    - **Step 7**: The resource server validates the access token and, if valid, serves the requested resources. If the token is invalid or expired, it returns an unauthorized error.
## OpenID Connect
1. **What is OpenID Connect?**
   - OpenID Connect (OIDC) is an identity layer built on top of the OAuth 2.0 protocol, enabling clients to verify the identity of the end-user based on the authentication performed by an authorization server, as well as to obtain basic profile information about the user.
2. **Key Components of OpenID Connect:**
   - **ID Token**: A JWT that contains information about the user (the subject) and is issued by the authorization server. It includes claims such as `iss` (issuer), `sub` (subject), `aud` (audience), and `exp` (expiration time).
   - **UserInfo Endpoint**: An endpoint provided by the authorization server that returns additional user profile information when accessed with a valid access token.
3. **OpenID Connect Flow: Authorization Code Flow**
   - **Step 1**: The client redirects the user to the authorization server's authorization endpoint, requesting authentication and authorization.
   - **Step 2**: The user authenticates and grants authorization to the client.
   - **Step 3**: The authorization server redirects the user back to the client with an authorization code.
   - **Step 4**: The client exchanges the authorization code for an ID token and an access token by making a request to the authorization server's token endpoint, including its client credentials.
    - **Step 5**: The authorization server validates the authorization code and client credentials, then issues an ID token and an access token to the client.
    - **Step 6**: The client verifies the ID token's signature and claims to authenticate the user.
    - **Step 7**: The client can optionally use the access token to request additional user profile information from the UserInfo endpoint.
    - **Step 8**: The client uses the ID token and any additional user information to personalize the user experience and manage user sessions.

## SAML
1. **What is SAML?**
   - Security Assertion Markup Language (SAML) is an XML-based framework for exchanging authentication and authorization data between parties, particularly between an identity provider (IdP) and a service provider (SP).
2. **Key Components of SAML:**
   - **Identity Provider (IdP)**: The entity that authenticates the user and issues SAML assertions.
   - **Service Provider (SP)**: The entity that provides services to the user and relies on the IdP for authentication.
   - **SAML Assertion**: An XML document that contains statements about the user, including authentication, attribute, and authorization statements.
3. **SAML Flow: Web Browser SSO Profile**
   - **Step 1**: The user attempts to access a protected resource on the service provider (SP).
   - **Step 2**: The SP redirects the user to the identity provider (IdP) for authentication.
   - **Step 3**: The user authenticates with the IdP (e.g., by entering credentials).
   - **Step 4**: Upon successful authentication, the IdP generates a SAML assertion containing user information and signs it.
   - **Step 5**: The IdP sends the SAML assertion to the SP, typically via the user's browser (e.g., using an HTTP POST).
   - **Step 6**: The SP receives the SAML assertion, verifies its signature, and extracts user information.
   - **Step 7**: The SP grants the user access to the protected resource based on the information in the SAML assertion.
## LDAP
1. **What is LDAP?**
   - Lightweight Directory Access Protocol (LDAP) is a protocol used to access and maintain distributed directory information services over an IP network. It is commonly used for storing user credentials and other organizational data.      
2. **Key Components of LDAP:**
   - **Directory**: A hierarchical structure that organizes entries (e.g., users, groups) in a tree-like format.
   - **Entry**: A single object in the directory, represented as a collection of attributes (e.g., username, email).
   - **Distinguished Name (DN)**: A unique identifier for an entry in the directory, specifying its position in the hierarchy.
   - **LDAP Server**: The server that hosts the directory and responds to LDAP queries.
3. **LDAP Authentication Flow:**
   - **Step 1**: The client application connects to the LDAP server.
   - **Step 2**: The client sends a bind request to the LDAP server, providing the user's DN and password.
   - **Step 3**: The LDAP server verifies the credentials against the stored entries in the directory.
   - **Step 4**: If the credentials are valid, the LDAP server responds with a success message, and the user is authenticated. If invalid, it responds with an error message.
   - **Step 5**: The client can then perform additional LDAP operations (e.g., search, modify) based on the authenticated user's permissions.   



