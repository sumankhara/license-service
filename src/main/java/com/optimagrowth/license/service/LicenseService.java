package com.optimagrowth.license.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;

@Service
public class LicenseService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	private ServiceConfig config;

	public License getLicense(String licenseId, String organizationId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if(null == license) {
			throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
		}
		return license.withComment(config.getExampleProperty());
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);
		
		return license.withComment(config.getExampleProperty());
	}

	public License updateLicense(License license) {
		licenseRepository.save(license);
		
		return license.withComment(config.getExampleProperty());
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
