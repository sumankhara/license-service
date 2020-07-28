package com.optimagrowth.license.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;

@Service
public class LicenseService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	private OrganizationDiscoveryClient organizationDiscoveryClient;
	
	@Autowired
	private OrganizationRestTemplateClient organizationRestClient;
	
	@Autowired
	private OrganizationFeignClient organizationFeignClient;

	public License getLicense(String licenseId, String organizationId, String clientType) {
		License license = licenseRepository.findByLicenseId(licenseId);
		if(null == license) {
			throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
		}
		
		Organization organization = retrieveOrganizationInfo(organizationId, clientType);
		if (null != organization) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}
		return license;
	}
	
	private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
		Organization organization = null;

		switch (clientType) {
		case "feign":
			System.out.println("I am using the feign client");
			organization = organizationFeignClient.getOrganization(organizationId);
			break;
		case "rest":
			System.out.println("I am using the rest client");
			organization = organizationRestClient.getOrganization(organizationId);
			break;
		case "discovery":
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestClient.getOrganization(organizationId);
			break;
		}

		return organization;
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);
		
		return license;
	}

	public License updateLicense(License license) {
		licenseRepository.save(license);
		
		return license;
	}

	public String deleteLicense(String licenseId) {
		String responseMessage = null;
		License license = new License();
		license.setLicenseId(licenseId);
		licenseRepository.delete(license);
		responseMessage = String.format("Deleting license with id %s", licenseId);
		
		return responseMessage;
	}
}
