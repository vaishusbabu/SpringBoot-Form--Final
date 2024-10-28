package com.example.Hospital_Form.entity;

import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "UserForm", uniqueConstraints = @UniqueConstraint(columnNames = "insuranceID"))
public class UserForm {

  @Id
  private String healthCareNumber;

  @NotNull(message = "Registration date is mandatory")
  private LocalDate registrationDate;

  @NotNull(message = "Registration time is mandatory")
  private String registrationTime;

  @NotBlank(message = "Patient's first name is mandatory")
  @Column(length = 55)
  private String patientFirstName;

  @NotBlank(message = "Patient's last name is mandatory")
  @Column(length = 55)
  private String patientLastName;

  @NotNull(message = "Sex is mandatory")
  @Pattern(regexp = "^(Female|Male|N/A)$", message = "Invalid sex option. Allowed values are Female, Male, N/A.")
  private String sex;

  @NotNull(message = "Birth month is mandatory")
  private String birthMonth;

  @NotNull(message = "Birth day is mandatory")
  @Min(value = 1, message = "Birth day must be at least 1.")
  @Max(value = 31, message = "Birth day must be at most 31.")
  private int birthDay;

  @NotNull(message = "Birth year is mandatory")
  @Min(value = 1900, message = "Birth year must be at least 1900.")
  @Max(value = 2024, message = "Birth year must be at most 2024.")
  private int birthYear;

  @NotNull(message = "Younger than 18 status is mandatory")
  private boolean isYoungerThan18;

  @NotNull(message = "Phone number is mandatory")
  @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "Phone number must be in the format (123) 456-7890.")
  private String phoneNumber;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Invalid email format.")
  @Column(length = 100)
  private String email;

  @NotBlank(message = "Password is mandatory")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
          message = "Password must be at least 8 characters long, include uppercase letters, lowercase letters, numbers, and special characters.")
  @Column(length = 100)
  private String password;

  @NotBlank(message = "Street address is mandatory")
  @Column(length = 100)
  private String streetAddress;

  @Column(length = 100)
  private String streetAddressLine2;

  @Column(length = 50)
  private String city;

  @NotBlank(message = "State or province is mandatory")
  @Column(length = 50)
  private String stateOrProvince;

  @Column(length = 20)
  private String postalOrZipCode;

  @NotNull(message = "Marital status is mandatory")
  @Pattern(regexp = "^(Single|Married|Divorced|Legally Separated|Widowed)$", message = "Marital status must be one of the following: Single, Married, Divorced, Legally Separated, Widowed.")
  private String maritalStatus;

  @NotBlank(message = "Emergency contact first name is mandatory")
  @Column(length = 55)
  private String emergencyContactFirstName;

  @NotBlank(message = "Emergency contact last name is mandatory")
  @Column(length = 55)
  private String emergencyContactLastName;

  @NotBlank(message = "Emergency contact relationship is mandatory")
  @Column(length = 55)
  private String emergencyContactRelationship;

  @NotBlank(message = "Emergency contact phone number is mandatory")
  @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "Phone number must be in the format (123) 456-7890.")
  private String emergencyContactPhoneNumber;

  @Column(length = 55)
  private String familyDoctorFirstName;

  @Column(length = 55)
  private String familyDoctorLastName;

  @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "Phone number must be in the format (123) 456-7890.")
  private String familyDoctorPhoneNumber;

  @Column(length = 55)
  private String preferredPharmacy;

  @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "Phone number must be in the format (123) 456-7890.")
  private String pharmacyPhoneNumber;

  @NotBlank(message = "Reason for registration is mandatory")
  @Column(length = 500)
  private String reasonForRegistration;

  @Column(length = 500)
  private String additionalNotes;

  @NotNull(message = "Taking medications status is mandatory")
  private boolean takingMedications;

  @Column(length = 55)
  private String insuranceCompany;

  @NotBlank(message = "Insurance ID is mandatory")
  @Pattern(regexp = "^H\\d{9}$", message = "Insurance ID must start with 'H' followed by 9 digits (e.g., H123456789).")
  @Column(length = 55)
  private String insuranceID; // Unique constraint added here

  @Column(length = 55)
  private String policyHolderFirstName;

  @Column(length = 55)
  private String policyHolderLastName;

  private LocalDate policyHolderDateOfBirth;

  private String otp = null;

  private LocalDateTime otpExpiration = null;

  @PrePersist
  protected void onCreate() {
    this.registrationDate = LocalDate.now();
    this.registrationTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    this.healthCareNumber = generateHealthCareNumber();
  }

  private String generateHealthCareNumber() {
    // Generate a unique 14-digit number
    Set<String> existingNumbers = new HashSet<>(); // Store existing numbers (you can fetch these from the database)

    Random random = new Random();
    String healthCareNumber;

    do {
      healthCareNumber = String.format("%014d", random.nextInt(100000000)); // Generate a random 14-digit number
    } while (existingNumbers.contains(healthCareNumber)); // Check for uniqueness

    existingNumbers.add(healthCareNumber); // Add the generated number to the set
    return healthCareNumber;
  }

  // Getters and Setters
  public String getHealthCareNumber() {
    return healthCareNumber;
  }

  public void setHealthCareNumber(String healthCareNumber) {
    this.healthCareNumber = healthCareNumber;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public String getRegistrationTime() {
    return registrationTime;
  }

  public void setRegistrationTime(String registrationTime) {
    this.registrationTime = registrationTime;
  }




  public String getPatientFirstName() {
    return patientFirstName;
  }

  public void setPatientFirstName(String patientFirstName) {
    this.patientFirstName = patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
  }

  public void setPatientLastName(String patientLastName) {
    this.patientLastName = patientLastName;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getBirthMonth() {
    return birthMonth;
  }

  public void setBirthMonth(String birthMonth) {
    this.birthMonth = birthMonth;
  }

  public int getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(int birthDay) {
    this.birthDay = birthDay;
  }

  public int getBirthYear() {
    return birthYear;
  }

  public void setBirthYear(int birthYear) {
    this.birthYear = birthYear;
  }

  public boolean isYoungerThan18() {
    return isYoungerThan18;
  }

  public void setYoungerThan18(boolean isYoungerThan18) {
    this.isYoungerThan18 = isYoungerThan18;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public String getStreetAddressLine2() {
    return streetAddressLine2;
  }

  public void setStreetAddressLine2(String streetAddressLine2) {
    this.streetAddressLine2 = streetAddressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateOrProvince() {
    return stateOrProvince;
  }

  public void setStateOrProvince(String stateOrProvince) {
    this.stateOrProvince = stateOrProvince;
  }

  public String getPostalOrZipCode() {
    return postalOrZipCode;
  }

  public void setPostalOrZipCode(String postalOrZipCode) {
    this.postalOrZipCode = postalOrZipCode;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public String getEmergencyContactFirstName() {
    return emergencyContactFirstName;
  }

  public void setEmergencyContactFirstName(String emergencyContactFirstName) {
    this.emergencyContactFirstName = emergencyContactFirstName;
  }

  public String getEmergencyContactLastName() {
    return emergencyContactLastName;
  }

  public void setEmergencyContactLastName(String emergencyContactLastName) {
    this.emergencyContactLastName = emergencyContactLastName;
  }

  public String getEmergencyContactRelationship() {
    return emergencyContactRelationship;
  }

  public void setEmergencyContactRelationship(String emergencyContactRelationship) {
    this.emergencyContactRelationship = emergencyContactRelationship;
  }

  public String getEmergencyContactPhoneNumber() {
    return emergencyContactPhoneNumber;
  }

  public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
    this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
  }

  public String getFamilyDoctorFirstName() {
    return familyDoctorFirstName;
  }

  public void setFamilyDoctorFirstName(String familyDoctorFirstName) {
    this.familyDoctorFirstName = familyDoctorFirstName;
  }

  public String getFamilyDoctorLastName() {
    return familyDoctorLastName;
  }

  public void setFamilyDoctorLastName(String familyDoctorLastName) {
    this.familyDoctorLastName = familyDoctorLastName;
  }

  public String getFamilyDoctorPhoneNumber() {
    return familyDoctorPhoneNumber;
  }

  public void setFamilyDoctorPhoneNumber(String familyDoctorPhoneNumber) {
    this.familyDoctorPhoneNumber = familyDoctorPhoneNumber;
  }

  public String getPreferredPharmacy() {
    return preferredPharmacy;
  }

  public void setPreferredPharmacy(String preferredPharmacy) {
    this.preferredPharmacy = preferredPharmacy;
  }

  public String getPharmacyPhoneNumber() {
    return pharmacyPhoneNumber;
  }

  public void setPharmacyPhoneNumber(String pharmacyPhoneNumber) {
    this.pharmacyPhoneNumber = pharmacyPhoneNumber;
  }

  public String getReasonForRegistration() {
    return reasonForRegistration;
  }

  public void setReasonForRegistration(String reasonForRegistration) {
    this.reasonForRegistration = reasonForRegistration;
  }

  public String getAdditionalNotes() {
    return additionalNotes;
  }

  public void setAdditionalNotes(String additionalNotes) {
    this.additionalNotes = additionalNotes;
  }

  public boolean isTakingMedications() {
    return takingMedications;
  }

  public void setTakingMedications(boolean takingMedications) {
    this.takingMedications = takingMedications;
  }

  public String getInsuranceCompany() {
    return insuranceCompany;
  }

  public void setInsuranceCompany(String insuranceCompany) {
    this.insuranceCompany = insuranceCompany;
  }

  public String getInsuranceID() {
    return insuranceID;
  }

  public void setInsuranceID(String insuranceID) {
    this.insuranceID = insuranceID;
  }

  public String getPolicyHolderFirstName() {
    return policyHolderFirstName;
  }

  public void setPolicyHolderFirstName(String policyHolderFirstName) {
    this.policyHolderFirstName = policyHolderFirstName;
  }

  public String getPolicyHolderLastName() {
    return policyHolderLastName;
  }

  public void setPolicyHolderLastName(String policyHolderLastName) {
    this.policyHolderLastName = policyHolderLastName;
  }

  public LocalDate getPolicyHolderDateOfBirth() {
    return policyHolderDateOfBirth;
  }

  public void setPolicyHolderDateOfBirth(LocalDate policyHolderDateOfBirth) {
    this.policyHolderDateOfBirth = policyHolderDateOfBirth;
  }

  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }

  public LocalDateTime getOtpExpiration() {
    return otpExpiration;
  }

  public void setOtpExpiration(LocalDateTime otpExpiration) {
    this.otpExpiration = otpExpiration;
  }
}
