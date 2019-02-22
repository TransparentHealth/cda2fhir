package tr.com.srdc.cda2fhir;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.consol.ProblemConcernAct;
import org.openhealthtools.mdht.uml.cda.consol.impl.ConsolFactoryImpl;
import org.openhealthtools.mdht.uml.cda.consol.impl.ProblemConcernActImpl;
import org.openhealthtools.mdht.uml.cda.consol.impl.ProblemObservationImpl;
import org.openhealthtools.mdht.uml.cda.consol.impl.ProblemStatusImpl;
import org.openhealthtools.mdht.uml.cda.impl.CDAFactoryImpl;
import org.openhealthtools.mdht.uml.cda.impl.EntryRelationshipImpl;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.impl.CDImpl;
import org.openhealthtools.mdht.uml.hl7.datatypes.impl.DatatypesFactoryImpl;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActRelationshipEntryRelationship;

import com.bazaarvoice.jolt.JsonUtils;

import tr.com.srdc.cda2fhir.testutil.BundleUtil;
import tr.com.srdc.cda2fhir.transform.ResourceTransformerImpl;

public class ProblemConcernActTest {
	private static final ResourceTransformerImpl rt = new ResourceTransformerImpl();
	
	private static ConsolFactoryImpl cdaObjFactory;
	private static DatatypesFactory cdaTypeFactory;
	private static CDAFactoryImpl cdaFactory;

	private static Map<String, String> cdaProblemStatusCodeToName = new HashMap<String, String>();

	private static Map<String, Object> clinicalStatusMap = JsonUtils.filepathToMap("src/test/resources/jolt/value-maps/ConditionClinicalStatus.json");
	private static Map<String, Object> verificationStatusMap = JsonUtils.filepathToMap("src/test/resources/jolt/value-maps/ConditionVerificationStatus.json");
		
	@BeforeClass
	public static void init() {
		CDAUtil.loadPackages();
		
		cdaProblemStatusCodeToName.put("55561003", "Active");
		cdaProblemStatusCodeToName.put("73425007", "Inactive");
		cdaProblemStatusCodeToName.put("413322009", "Resolved");

		cdaObjFactory = (ConsolFactoryImpl) ConsolFactoryImpl.init();
		cdaTypeFactory = DatatypesFactoryImpl.init();		
		cdaFactory = (CDAFactoryImpl) CDAFactoryImpl.init();
	}

	static private ProblemStatusImpl createProblemStatus(String cdaProblemStatusCode) {
		ProblemStatusImpl problemStatus = (ProblemStatusImpl) cdaObjFactory.createProblemStatus();	
		
		II templateId = cdaTypeFactory.createII("2.16.840.1.113883.10.20.22.4.6");
		problemStatus.getTemplateIds().add(templateId);

		CD code = cdaTypeFactory.createCD("33999-4", "2.16.840.1.113883.6.1", null, null);
		problemStatus.setCode(code);

		CS cs = cdaTypeFactory.createCS("completed");
		problemStatus.setStatusCode(cs);
		if (cdaProblemStatusCode == null) {
			cdaProblemStatusCode = clinicalStatusMap.entrySet().stream().findFirst().get().getKey();
		}
		String cdaProblemStatusName = cdaProblemStatusCodeToName.get(cdaProblemStatusCode);	
		CE ce = cdaTypeFactory.createCE (cdaProblemStatusCode, "2.16.840.1.11388 3.6.96", "SNOMED CT", cdaProblemStatusName);
		problemStatus.getValues().add(ce);
		
		return problemStatus;
	}
	
	private static ProblemConcernActImpl createProblemConcernAct() {
		ProblemConcernActImpl act = (ProblemConcernActImpl) cdaObjFactory.createProblemConcernAct();
		ProblemObservationImpl observation = (ProblemObservationImpl) cdaObjFactory.createProblemObservation();
		act.addObservation(observation);
		act.getEntryRelationships().stream()
			.filter(r -> (r.getObservation() == observation))
			.forEach(r -> r.setTypeCode(x_ActRelationshipEntryRelationship.SUBJ));
		return act;
	}

	static private void verifyCoding(Coding coding, String code, String displayName, String system) {
		Assert.assertEquals("Unexpected Coding code", code, coding.getCode());
		Assert.assertEquals("Unexpected Coding display name", displayName, coding.getDisplay());
		Assert.assertEquals("Unexpected Coding system", system, coding.getSystem());
	}
	
	@Test
	public void testProblemObservationCode() throws Exception {
		ProblemConcernActImpl act = createProblemConcernAct();
		ProblemObservationImpl observation =  (ProblemObservationImpl) act.getEntryRelationships().get(0).getObservation();
		DiagnosticChain dxChain = new BasicDiagnostic();

		String code = "404684003"; // From CCDA Specification
		String displayName = "Finding";		
		CDImpl cd = (CDImpl) cdaTypeFactory.createCD(code, "2.16.840.1.113883.6.96", "SNOMED CT", displayName);
		observation.setCode(cd);
		
		Boolean validation = act.validateProblemConcernActProblemObservation(dxChain, null);
		Assert.assertTrue("Invalid Problem Concern Act in Test", validation);

		Bundle bundle = rt.tProblemConcernAct2Condition(act);
		Condition condition = BundleUtil.findOneResource(bundle, Condition.class);
		List<Coding> category = condition.getCategory().get(0).getCoding();
		Assert.assertEquals("Unexpected number of category codings", 1, category.size());
		verifyCoding(category.get(0), code, displayName, "http://snomed.info/sct");
		
		String translationCode = "75321-0"; // From CCDA Specification
		String translationDisplayName = "Clinical finding HL7.CCDAR2";
		CD translationCd = cdaTypeFactory.createCD(translationCode, "2.16.840.1.113883.6.1", "LOINC", translationDisplayName);
		cd.getTranslations().add(translationCd);

		Boolean validation2 = act.validateProblemConcernActProblemObservation(dxChain, null);
		Assert.assertTrue("Invalid Problem Concern Act in Test", validation2);

		Bundle bundle2 = rt.tProblemConcernAct2Condition(act);
		Condition condition2 = BundleUtil.findOneResource(bundle2, Condition.class);
		List<Coding> category2 = condition2.getCategory().get(0).getCoding();
		Assert.assertEquals("Unexpected number of category codings", 2, category2.size());
		verifyCoding(category2.get(0), code, displayName, "http://snomed.info/sct");
		verifyCoding(category2.get(1), translationCode, translationDisplayName, "http://loinc.org");
	}
		
	@Test
	public void testProblemObservationProblemStatus() throws Exception {
		ProblemConcernActImpl act = createProblemConcernAct();
		ProblemObservationImpl observation =  (ProblemObservationImpl) act.getEntryRelationships().get(0).getObservation();
		
		for (Map.Entry<String, Object> entry : clinicalStatusMap.entrySet()) {
			String cdaProblemStatusCode = entry.getKey();
			String fhirClinicalStatus = (String) entry.getValue();
										
			ProblemStatusImpl problemStatus = createProblemStatus(cdaProblemStatusCode);	
			EntryRelationshipImpl entryRelationship = (EntryRelationshipImpl) cdaFactory.createEntryRelationship();
			entryRelationship.setObservation(problemStatus);
			entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.REFR);
			
			observation.getEntryRelationships().clear();
			observation.getEntryRelationships().add(entryRelationship);
										
			DiagnosticChain dxChain = new BasicDiagnostic();
			Boolean validation = act.validateProblemConcernActProblemObservation(dxChain, null);
			Assert.assertTrue("Invalid Problem Concern Act in Test", validation);
	
			Bundle bundle = rt.tProblemConcernAct2Condition(act);
			Condition condition = BundleUtil.findOneResource(bundle, Condition.class);
			ConditionClinicalStatus clinicalStatus = condition.getClinicalStatus();
			String actual = clinicalStatus.toCode();
			Assert.assertEquals("Unexpected Problem Concern Act Status", fhirClinicalStatus, actual);
		}
	}

	static private void verifyConditionVerificationStatus(ProblemConcernAct act, String expected) throws Exception {
		Bundle bundle = rt.tProblemConcernAct2Condition(act);
		Condition condition = BundleUtil.findOneResource(bundle, Condition.class);
		
    	ConditionVerificationStatus verificationStatus = condition.getVerificationStatus();
    	String actual = verificationStatus == null ? null : verificationStatus.toCode();
		Assert.assertEquals(expected, actual);		
	}
		
	@Test
	public void testStatusCode() throws Exception {
		ProblemConcernActImpl act = (ProblemConcernActImpl) cdaObjFactory.createProblemConcernAct();
		ProblemObservationImpl observation = (ProblemObservationImpl) cdaObjFactory.createProblemObservation();
		act.addObservation(observation);
				
		DiagnosticChain dxChain = new BasicDiagnostic();
		verifyConditionVerificationStatus(act, "unknown");
		
		act.setStatusCode(null);
		verifyConditionVerificationStatus(act, "unknown");
				
		act.setStatusCode(cdaTypeFactory.createCS("invalid"));
		Boolean invalidation = act.validateProblemConcernActStatusCode(null, null);
		Assert.assertFalse("Unexpected Valid Problem Concern Act in Test", invalidation) ;		
		
		CS csNullFlavor = cdaTypeFactory.createCS();
		csNullFlavor.setNullFlavor(NullFlavor.UNK);
		act.setStatusCode(csNullFlavor);
		Boolean validationNF = act.validateProblemConcernActStatusCode(dxChain, null);
		Assert.assertTrue("Invalid Problem Concern Act in Test", validationNF);
		verifyConditionVerificationStatus(act, "unknown");
			
		for (Map.Entry<String, Object> entry : verificationStatusMap.entrySet()) {
			String cdaStatusCode = entry.getKey();
			String fhirStatus = (String) entry.getValue();
			
			CS cs = cdaTypeFactory.createCS(cdaStatusCode);
			act.setStatusCode(cs);
			Boolean validation = act.validateProblemConcernActStatusCode(dxChain, null);
			Assert.assertTrue("Invalid Problem Concern Act in Test", validation);

			verifyConditionVerificationStatus(act, fhirStatus);
		}
	}
}