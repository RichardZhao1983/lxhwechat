package com.lxh.wechat.security.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.signature.impl.X509CertificateBuilder;
import org.opensaml.xml.signature.impl.X509DataBuilder;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import com.lxh.wechat.config.SamlProperties;
import com.lxh.wechat.exception.OAuthException;
import com.lxh.wechat.exception.WeChatIntegrationException;
import com.lxh.wechat.security.SAMLAssertionService;

@Service
public class SAMLAssertionServiceImpl implements SAMLAssertionService {
	private final static Logger LOGGER = LoggerFactory.getLogger(SAMLAssertionServiceImpl.class);

	@Autowired
	private SamlProperties samlProperties;

	private PublicKey publicKey;
	private PrivateKey privateKey;
	private X509Certificate certificate;
	private Credential credential;

	private static XMLObjectBuilderFactory builderFactory;
	private Credential _signingCredential;
	static private SAMLObjectBuilder nameIdBuilder = null;
	static private SAMLObjectBuilder confirmationMethodBuilder = null;
	static private SAMLObjectBuilder subjectConfirmationBuilder = null;
	static private SAMLObjectBuilder subjectBuilder = null;
	static private SAMLObjectBuilder attrStatementBuilder = null;
	static private SAMLObjectBuilder audienceRestrictionnBuilder = null;
	static private SAMLObjectBuilder audienceBuilder = null;
	static private SAMLObjectBuilder authStatementBuilder = null;
	static private SAMLObjectBuilder authContextBuilder = null;
	static private SAMLObjectBuilder authContextClassRefBuilder = null;
	static private SAMLObjectBuilder issuerBuilder = null;
	static private SAMLObjectBuilder assertionBuilder = null;

	@Override
	public String createAssertion(final String solmanEndUserId) throws OAuthException {
		try {
			LOGGER.info(samlProperties.getKs_alias());
			Assertion assertion = createAssertionObject(solmanEndUserId);
			AssertionMarshaller marshaller = new AssertionMarshaller();
			Element plaintextElement = marshaller.marshall(assertion);
			String originalAssertionString = XMLHelper.nodeToString(plaintextElement);

			Credential signingCredential = getCredential();

			Signature signature = (Signature) getSAMLBuilder().getBuilder(Signature.DEFAULT_ELEMENT_NAME)
					.buildObject(Signature.DEFAULT_ELEMENT_NAME);

			signature.setSigningCredential(signingCredential);
			signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
			signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

			KeyInfoBuilder keyInfoBuilder = (KeyInfoBuilder) getSAMLBuilder().getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME);
			KeyInfo keyInfo = keyInfoBuilder.buildObject();

			X509DataBuilder x509databuilder = (X509DataBuilder) getSAMLBuilder()
					.getBuilder(X509Data.DEFAULT_ELEMENT_NAME);

			X509Data x509Data = x509databuilder.buildObject();
			X509CertificateBuilder x509CertificateBuilder = (X509CertificateBuilder) getSAMLBuilder()
					.getBuilder(org.opensaml.xml.signature.X509Certificate.DEFAULT_ELEMENT_NAME);

			org.opensaml.xml.signature.X509Certificate certXMLAssertion = x509CertificateBuilder.buildObject();

			certXMLAssertion.setValue(Base64.encodeBytes(signingCredential.getPublicKey().getEncoded()));
			x509Data.getX509Certificates().add(certXMLAssertion);
			keyInfo.getX509Datas().add(x509Data);
			signature.setKeyInfo(keyInfo);

			assertion.setSignature(signature);

			Configuration.getMarshallerFactory().getMarshaller(assertion).marshall(assertion);

			Signer.signObject(signature);

			plaintextElement = marshaller.marshall(assertion);
			originalAssertionString = XMLHelper.nodeToString(plaintextElement);
			return originalAssertionString;
		} catch (Exception e) {
			throw new OAuthException(e);
		}
	}

	@PostConstruct
	private static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException {

		if (builderFactory == null) {
			// OpenSAML 2.3
			DefaultBootstrap.bootstrap();
			builderFactory = Configuration.getBuilderFactory();
			nameIdBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(NameID.DEFAULT_ELEMENT_NAME);
			confirmationMethodBuilder = (SAMLObjectBuilder) getSAMLBuilder()
					.getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
			subjectConfirmationBuilder = (SAMLObjectBuilder) getSAMLBuilder()
					.getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
			subjectBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Subject.DEFAULT_ELEMENT_NAME);
			attrStatementBuilder = (SAMLObjectBuilder) getSAMLBuilder()
					.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
			audienceRestrictionnBuilder = (SAMLObjectBuilder) getSAMLBuilder()
					.getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);
			audienceBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Audience.DEFAULT_ELEMENT_NAME);
			authStatementBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
			authContextBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
			authContextClassRefBuilder = (SAMLObjectBuilder) getSAMLBuilder()
					.getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			issuerBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
			assertionBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

		}

		return builderFactory;
	}

	private Assertion createAssertionObject(String solmanEndUserId)
			throws ConfigurationException, WeChatIntegrationException {
		// Create the NameIdentifier
		NameID nameId = (NameID) nameIdBuilder.buildObject();
		nameId.setValue(solmanEndUserId);
		nameId.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");

		// Create the SubjectConfirmation

		SubjectConfirmationData confirmationMethod = (SubjectConfirmationData) confirmationMethodBuilder.buildObject();

		DateTime now = new DateTime();
		DateTime until = new DateTime().plusHours(4);

		// confirmationMethod.setNotBefore(now);
		confirmationMethod.setNotOnOrAfter(until);
		confirmationMethod.setRecipient(samlProperties.getOa2_token_endpoint());
		SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();

		subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
		subjectConfirmation.setSubjectConfirmationData(confirmationMethod);

		// should be Bearer

		// Create the Subject
		Subject subject = (Subject) subjectBuilder.buildObject();

		subject.setNameID(nameId);
		subject.getSubjectConfirmations().add(subjectConfirmation);

		// Builder Attributes
		AttributeStatement attrStatement = (AttributeStatement) attrStatementBuilder.buildObject();

		// Create the audience restriction
		AudienceRestriction audienceRestriction = (AudienceRestriction) audienceRestrictionnBuilder.buildObject();

		// Create the audience
		Audience audience = (Audience) audienceBuilder.buildObject();
		audience.setAudienceURI(samlProperties.getSaml_audience_restriction());
		// add in the audience
		audienceRestriction.getAudiences().add(audience);

		SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) getSAMLBuilder()
				.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
		Conditions conditions = (Conditions) conditionsBuilder.buildObject();

		// conditions.getConditions().add(condition);
		conditions.getAudienceRestrictions().add(audienceRestriction);
		conditions.setNotBefore(now);
		conditions.setNotOnOrAfter(until);

		// Authnstatement

		AuthnStatement authnStatement = (AuthnStatement) authStatementBuilder.buildObject();
		// authnStatement.setSubject(subject);
		// authnStatement.setAuthenticationMethod(strAuthMethod);
		DateTime now2 = new DateTime();
		authnStatement.setAuthnInstant(now2);
		// authnStatement.setSessionIndex(input.getSessionId());
		authnStatement.setSessionNotOnOrAfter(now2.plus(15));

		AuthnContext authnContext = (AuthnContext) authContextBuilder.buildObject();

		AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authContextClassRefBuilder.buildObject();
		authnContextClassRef.setAuthnContextClassRef(samlProperties.getSaml_session_authentication());

		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);

		// Create Issuer
		Issuer issuer = (Issuer) issuerBuilder.buildObject();
		issuer.setValue(samlProperties.getSaml_issuer());

		// Create the attribute
		AttributeStatementBuilder attributeStatementBuilder = (AttributeStatementBuilder) builderFactory
				.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
		AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();

		AttributeBuilder attributeBuilder = (AttributeBuilder) builderFactory
				.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
		Attribute attr = attributeBuilder.buildObject();
		attr.setName("client_id");

		XSAnyBuilder sb2 = (XSAnyBuilder) builderFactory.getBuilder(XSAny.TYPE_NAME);
		XSAny attrAny = sb2.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSAny.TYPE_NAME);
		attrAny.setTextContent(samlProperties.getOa2_client_id());

		attr.getAttributeValues().add(attrAny);
		attributeStatement.getAttributes().add(attr);

		// Create the assertion
		Assertion assertion = (Assertion) assertionBuilder.buildObject();
		assertion.setID("_" + UUID.randomUUID().toString());
		assertion.setSubject(subject);
		assertion.setIssuer(issuer);
		assertion.setIssueInstant(now);
		assertion.getAttributeStatements().add(attributeStatement);
		assertion.getAuthnStatements().add(authnStatement);
		assertion.setVersion(SAMLVersion.VERSION_20);

		assertion.setConditions(conditions);

		return assertion;
	}

	private KeyStore getKeyStore() throws WeChatIntegrationException, KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance(samlProperties.getKs_type());
		InputStream in = getClass().getResourceAsStream(samlProperties.getKs_resource());
		ks.load(in, samlProperties.getKs_pwd().toCharArray());
		return ks;
	}

	private X509Certificate getCertificate(KeyStore ks) throws IOException, NoSuchAlgorithmException,
			CertificateException, WeChatIntegrationException, KeyStoreException, NoSuchProviderException {
		if (this.certificate == null) {
			this.certificate = (X509Certificate) ks.getCertificate(samlProperties.getKs_alias());
		}
		return this.certificate;
	}

	private PrivateKey getPrivateKey(KeyStore ks)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, WeChatIntegrationException {
		if (this.privateKey == null) {
			this.privateKey = (PrivateKey) ks.getKey(samlProperties.getKs_alias(),
					samlProperties.getKs_pwd().toCharArray());
		}
		return this.privateKey;
	}

	private PublicKey getPublicKey(KeyStore ks) throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, WeChatIntegrationException, NoSuchProviderException {
		if (this.publicKey == null) {
			this.publicKey = this.getCertificate(ks).getPublicKey();

		}
		return this.publicKey;
	}

	private Credential getCredential() throws IOException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, UnrecoverableKeyException, WeChatIntegrationException, NoSuchProviderException {
		if (this.credential == null) {
			KeyStore ks = getKeyStore();
			this.credential = SecurityHelper.getSimpleCredential(this.getPublicKey(ks), this.getPrivateKey(ks));
		}
		return this.credential;
	}
}
