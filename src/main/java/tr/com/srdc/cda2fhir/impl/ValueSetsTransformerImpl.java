package tr.com.srdc.cda2fhir.impl;

import org.openhealthtools.mdht.uml.hl7.vocab.EntityClassRoot;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityNameUse;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;
import org.openhealthtools.mdht.uml.hl7.vocab.TelecommunicationAddressUse;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.GroupTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ProcedureStatusEnum;
import tr.com.srdc.cda2fhir.ValueSetsTransformer;

public class ValueSetsTransformerImpl implements ValueSetsTransformer {
	
	public String oid2Url(String codeSystem){
		String system = null;
		switch (codeSystem) {
	        case "2.16.840.1.113883.6.96":
	            system = "http://snomed.info/sct";
	            break;
	        case "2.16.840.1.113883.6.88":
	            system = "http://www.nlm.nih.gov/research/umls/rxnorm";
	            break;
	        case "2.16.840.1.113883.6.1":
	            system = "http://loinc.org";
	            break;
	        case "2.16.840.1.113883.6.8":
	            system = "http://unitsofmeasure.org";
	            break;
	        case "2.16.840.1.113883.3.26.1.2":
	            system = "http://ncimeta.nci.nih.gov";
	            break;
	        case "2.16.840.1.113883.6.12":
	            system = "http://www.ama-assn.org/go/cpt";
	            break;
	        case "2.16.840.1.113883.6.209":
	            system = "http://hl7.org/fhir/ndfrt";
	            break;
	        case "2.16.840.1.113883.4.9":
	            system = "http://fdasis.nlm.nih.gov";
	            break;
	        case "2.16.840.1.113883.12.292":
	            system = "http://www2a.cdc.gov/vaccines/iis/iisstandards/vaccines.asp?rpt=cvx";
	            break;
	        case "1.0.3166.1.2.2":
	            system = "urn:iso:std:iso:3166";
	            break;
	        case "2.16.840.1.113883.6.301.5":
	            system = "http://www.nubc.org/patient-discharge";
	            break;
	        case "2.16.840.1.113883.6.256":
	            system = "http://www.radlex.org";
	            break;
	        case "2.16.840.1.113883.6.3":
	            system = "http://hl7.org/fhir/sid/icd-10";
	            break;
	        case "2.16.840.1.113883.6.4":
	            system = "http://www.icd10data.com/icd10pcs";
	            break;
	        case "2.16.840.1.113883.6.42":
	            system = "http://hl7.org/fhir/sid/icd-9";
	            break;
	        case "2.16.840.1.113883.6.73":
	            system = "http://www.whocc.no/atc";
	            break;
	        case "2.16.840.1.113883.6.24":
	            system = "urn:std:iso:11073:10101";
	            break;
	        case "1.2.840.10008.2.16.4":
	            system = "http://nema.org/dicom/dicm";
	            break;
	        case "2.16.840.1.113883.6.281":
	            system = "http://www.genenames.org";
	            break;
	        case "2.16.840.1.113883.6.280":
	            system = "http://www.ncbi.nlm.nih.gov/nuccore";
	            break;
	        case "2.16.840.1.113883.6.282":
	            system = "http://www.hgvs.org/mutnomen";
	            break;
	        case "2.16.840.1.113883.6.284":
	            system = "http://www.ncbi.nlm.nih.gov/projects/SNP";
	            break;
	        case "2.16.840.1.113883.3.912":
	            system = "http://cancer.sanger.ac.uk/cancergenome/projects/cosmic";
	            break;
	        case "2.16.840.1.113883.6.283":
	            system = "http://www.hgvs.org/mutnomen";
	            break;
	        case "2.16.840.1.113883.6.174":
	            system = "http://www.omim.org";
	            break;
	        case "2.16.840.1.113883.13.191":
	            system = "http://www.ncbi.nlm.nih.gov/pubmed";
	            break;
	        case "2.16.840.1.113883.3.913":
	            system = "http://www.pharmgkb.org";
	            break;
	        case "2.16.840.1.113883.3.1077":
	            system = "http://clinicaltrials.gov";
	            break;
	        default:
	            system = "urn:oid:" + codeSystem;
	            break;
        }
		return system;
	}
	
	public ProcedureStatusEnum StatusCode2ProcedureStatusEnum( String statusCodeString ){
		switch( statusCodeString.toLowerCase() ){
		case "active":
			return ProcedureStatusEnum.IN_PROGRESS;
		case "completed":
			return ProcedureStatusEnum.COMPLETED;
		case "aborted":
		case "aboted":
			return ProcedureStatusEnum.ABOTED;
		case "error":
			return ProcedureStatusEnum.ENTERED_IN_ERROR;
		default:
			return null;
		}
	}
	
	public GroupTypeEnum EntityClassRoot2GroupTypeEnum( EntityClassRoot entityClassRoot ){
		switch(entityClassRoot){
			case PSN: return GroupTypeEnum.PERSON;
			case ANM: return GroupTypeEnum.ANIMAL;
			case DEV: return GroupTypeEnum.DEVICE;
			case MMAT: return GroupTypeEnum.MEDICATION;
			default: return null;
		}
	}
	
	public MaritalStatusCodesEnum MaritalStatusCode2MaritalStatusCodesEnum( String maritalStatusCode ){
		// Visit https://www.hl7.org/fhir/valueset-marital-status.html
		switch(maritalStatusCode){
			case "A": return MaritalStatusCodesEnum.A;
			case "D": return MaritalStatusCodesEnum.D;
			case "I": return MaritalStatusCodesEnum.I;
			case "L": return MaritalStatusCodesEnum.L;
			case "M": return MaritalStatusCodesEnum.M;
			case "P": return MaritalStatusCodesEnum.P;
			case "S": return MaritalStatusCodesEnum.S;
			case "T": return MaritalStatusCodesEnum.T;
			case "W": return MaritalStatusCodesEnum.W;
			case "UNK":
			case "U": 
			default:
				return MaritalStatusCodesEnum.UNK;
		}
	}
	
	public AdministrativeGenderEnum AdministrativeGenderCode2AdministrativeGenderEnum( String administrativeGenderCode ){
		// Visit https://www.hl7.org/fhir/valueset-administrative-gender.html
		switch (administrativeGenderCode) {
			case "F": // Female
			case "f":
				return AdministrativeGenderEnum.FEMALE;
			case "M": // Male
			case "m":
				return AdministrativeGenderEnum.MALE;
			case "U": // Undifferentiated
			case "u":
			case "UN":
			case "UNK":
			case "un":
			case "unk":
				return AdministrativeGenderEnum.UNKNOWN;
			default:
				return AdministrativeGenderEnum.UNKNOWN;
		} // end of switch block
	}

	public MedicationStatementStatusEnum StatusCode2MedicationStatementStatusEnum( String status){
		switch( status ){
			case "active": return MedicationStatementStatusEnum.ACTIVE;
			
			case "completed": return MedicationStatementStatusEnum.COMPLETED;
			case "nullified": return MedicationStatementStatusEnum.ENTERED_IN_ERROR;
			
			default: return null;
		}
	}
	
	public MedicationDispenseStatusEnum StatusCode2MedicationDispenseStatusEnum( String status){
		switch( status ){
			case "active": return MedicationDispenseStatusEnum.IN_PROGRESS;
			case "suspended": return MedicationDispenseStatusEnum.ON_HOLD;
			case "completed": return MedicationDispenseStatusEnum.COMPLETED;
			case "nullified": return MedicationDispenseStatusEnum.ENTERED_IN_ERROR;
			case "stopped": return MedicationDispenseStatusEnum.STOPPED;
			default: return null;
		}
	}
	
	public NameUseEnum EntityNameUse2NameUseEnum(EntityNameUse entityNameUse){
		
		switch(entityNameUse){
			case C: return NameUseEnum.USUAL;
			// Visit https://www.hl7.org/fhir/valueset-name-use.html
			// Trying: case OR: return NameUseEnum.OFFICIAL;
			// .. T, ANON, OLD, M.
			// However, these cases don't exist
			case P: return NameUseEnum.NICKNAME;
			default: return NameUseEnum.USUAL;
		}
	}
	
	public AddressUseEnum PostalAdressUse2AddressUseEnum(PostalAddressUse postalAddressUse){
		
		switch(postalAddressUse){
			case HP:
			case H: return AddressUseEnum.HOME;
			case WP: return AddressUseEnum.WORK;
			case TMP: return AddressUseEnum.TEMPORARY;
			case BAD: return AddressUseEnum.OLD___INCORRECT;
		default:
			return AddressUseEnum.TEMPORARY;
		}
		
	}
	
	public ContactPointUseEnum TelecommunicationAddressUse2ContacPointUseEnum( TelecommunicationAddressUse telecommunicationAddressUse )
	{
		switch(telecommunicationAddressUse){
			case H: return ContactPointUseEnum.HOME;
			// new code start
			case HP: return ContactPointUseEnum.HOME;
			// new code end
			case WP: return ContactPointUseEnum.WORK;
			case TMP: return ContactPointUseEnum.TEMP;
			case BAD: return ContactPointUseEnum.OLD;
			case MC: return ContactPointUseEnum.MOBILE;
			default:
				return ContactPointUseEnum.TEMP;
		}
			
	}
	
	public CodingDt ParticipationType2ParticipationTypeCode(org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType cdaPT){
		CodingDt fhirPT = new CodingDt(); // fhirPT: fhirParticipationTypeCode
		fhirPT.setSystem("http://hl7.org/fhir/v3/ParticipationType");
		String code = null;
		String display = null;
		
		switch( cdaPT ){
			case PRF: code = "PRF"; display = "performer"; 
					break;
			case SBJ: code = "SBJ"; display ="subject"; 
					break;
			case ADM: code = "ADM"; display = "admitter";
					break;
			case ATND: code = "ATND"; display = "attender";
					break;
			case AUT: code = "AUT"; display = "author";
					break;
			case AUTHEN: code = "AUTHEN"; display = "authenticator";
				break;
			case BBY: code = "BBY"; display = "baby";
				break;
			case BEN: code = "BEN"; display = "beneficiary";
				break;
			case CALLBCK: code = "CALLBCK"; display = "callback contact";
				break;
			case CON: code = "CON"; display = "consultant";
				break;
			case COV: code = "COV"; display = "coverage target";
				break;
			case CSM: code = "CSM"; display = "consumable";
				break;
			case CST: code = "CST"; display = "custodian";
				break;
			case DEV: code = "DEV"; display = "device";
				break;
			case DIR: code = "DIR"; display = "direct target";
				break;
			case DIS: code = "DIS"; display = "discharger";
				break;
			case DIST: code = "DIST"; display = "distributor";
				break;
			case DON: code = "DON"; display = "donor";
				break;
			case DST: code = "DST"; display = "destination";
				break;
			case ELOC: code = "ELOC"; display = "entry location";
				break;
			case ENT: code = "ENT"; display = " data entry person";
				break;
			case ESC: code = "ESC"; display = "escort";
				break;
			case HLD: code = "HLD"; display = "holder";
				break;
			case IND: code = "IND"; display = "indirect target";
				break;
			case INF: code = "INF"; display = "informant";
				break;
			case IRCP: code = "IRCP"; display = "information recipient";
				break;
			case LA: code = "LA"; display = "legal authenticator";
				break;
			case LOC: code = "LOC"; display = "location";
				break;
			case NOT: code = "NOT"; display = "ugent notification contact";
				break;
			case NRD: code = "NRD"; display = "non-reuseable device";
				break;
			case ORG: code = "ORG"; display = "origin";
				break;
			case PPRF: code = "PPRF"; display = "primary performer";
				break;
			case PRCP: code = "PRCP"; display = "primary information recipient";
				break;
			case PRD: code = "PRD"; display = "product";
				break;
			case RCT: code = "RCT"; display = "record target";
				break;
			case RCV: code = "RCV"; display = "receiver";
				break;
			case RDV: code = "RDV"; display = "reusable device";
				break;
			case REF: code = "REF"; display = "referrer";
				break;
			case REFB: code = "REFB"; display = "Referred By";
				break;
			case REFT: code = "REFT"; display = "Referred to";
				break;
			case RESP: code = "RESP"; display = "responsible party";
				break;
			case RML: code = "RML"; display = "remote";
				break;
			case SPC: code = "SPC"; display = "specimen";
				break;
			case SPRF: code = "SPRF"; display = "secondary performer";
				break;
			case TRC: code = "TRC"; display = "tracker";
				break;
			case VIA: code = "VIA"; display = "via";
				break;
			case VRF: code = "VRF"; display = "verifier";
				break;
			case WIT: code = "WIT"; display = "witness";
				break;
			default:
				break;
		}
		if( code != null && display != null){
			fhirPT.setCode(code);
			fhirPT.setDisplay(display);
		}
		return fhirPT;
	}
	
	public AddressTypeEnum PostalAddressUse2AddressTypeEnum( PostalAddressUse postalAddressUse ){
		switch(postalAddressUse){
			case PHYS: return AddressTypeEnum.PHYSICAL;
			case PST: return AddressTypeEnum.POSTAL;
			default: return null;
		}
	}


	public LocationStatusEnum StatusCode2LocationStatusEnum(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	

}
