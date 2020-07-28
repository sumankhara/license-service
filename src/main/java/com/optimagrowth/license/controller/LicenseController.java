package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

	/*@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		License license = licenseService.getLicense(licenseId, organizationId);
		return ResponseEntity.ok(license);
	}*/
	
	@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		License license = licenseService.getLicense(licenseId, organizationId, "");
		license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                  linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
                  linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense"));
		return ResponseEntity.ok(license);
	}

	@PostMapping
	public ResponseEntity<License> createLicense(@RequestBody License license) {
		return ResponseEntity.ok(licenseService.createLicense(license));
	}

	@PutMapping
	public ResponseEntity<License> updateLicense(@RequestBody License license) {
		return ResponseEntity.ok(licenseService.updateLicense(license));
	}

	@DeleteMapping(value = "/{licenseId}")
	public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
		return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
	}
	
	@RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.POST)
	public License getLicenseWithClient(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,
			@PathVariable("clientType") String clientType) {
		return licenseService.getLicense(licenseId, organizationId, clientType);
	}
}
