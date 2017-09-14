package com.tii.php.util.builder;

import com.tii.php.entity.*;
import com.tii.php.entity.medlibrary.Medication;
import com.tii.php.service.*;
import com.tii.php.types.ContactPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by sbertiaux on 9/20/16.
 */
@Component
@Transactional
public class EntityBuilderService {

    private UserService userService;
    private ClientService clientService;
    private MedicationService medicationService;
    private ProfessionalService professionalService;
    private PatientProfessionalService patientProfessionalService;
    private AdvocateService advocateService;

    @Autowired
    public EntityBuilderService(UserService userService, ClientService clientService,
                                MedicationService medicationService, ProfessionalService professionalService,
                                PatientProfessionalService patientProfessionalService, AdvocateService advocateService) {
        this.userService = userService;
        this.clientService = clientService;
        this.medicationService = medicationService;
        this.professionalService = professionalService;
        this.patientProfessionalService = patientProfessionalService;
        this.advocateService = advocateService;
    }

    public EntityBuilder initialize(EntityBuilder entityBuilder) {
        for (int i = 0; i < entityBuilder.getNumClients(); i++) {
            entityBuilder.getClients().add(clientService.create(new Client(String.format("EntityBuilderClient%d", i))));
        }

        int clientIndex = 0;

        for (int i = 0; i < entityBuilder.getNumProfessionals(); i++) {
            clientIndex = i % entityBuilder.getClients().size();

            createProfessional(entityBuilder, clientIndex, i);
        }

        for (int i = 0; i < entityBuilder.getNumMedications(); i++) {
            createMedication(entityBuilder, i);
        }

        clientIndex = 0;
        int professionalIndex = 0;

        for (int i = 0; i < entityBuilder.getNumPatients(); i++) {
            clientIndex = i % entityBuilder.getClients().size();
            professionalIndex = i % entityBuilder.getProfessionals().size();

            Patient patient = createPatient(entityBuilder, clientIndex, professionalIndex, i);

            for (int p = 0; p < entityBuilder.getNumPatientProfessionals(); p++) {
                patient.addPatientProfessional(createPatientProfessional(entityBuilder, professionalIndex, patient));
            }

            for (int z = 0; z < entityBuilder.getNumAdvocates(); z++) {
                patient.addAdvocate(createAdvocate(entityBuilder, patient, z));
            }

            int medicationIndex = 0;

            for (int j = 0; j < entityBuilder.getNumMedicationInstances(); j++) {
                medicationIndex = j % entityBuilder.getMedications().size();

                createMedicationInstance(entityBuilder, patient, medicationIndex, professionalIndex);
            }
        }

        entityBuilder.setInitialized(true);

        return entityBuilder;
    }

    private Professional createProfessional(EntityBuilder entityBuilder, int clientIndex, int i) {
        ContactInfo contactInfo = new ContactInfo("1234567890",
                "0987654321",
                "5647382910",
                String.format("professionalemail%d@builder.com", i),
                ContactPreference.EMAIL);
        contactInfo.setTimeZone("America/Denver");

        Professional professional = new Professional();
        professional.setCareTeam("PCP");
        professional.setDoctorName(String.format("EntityBuilderProfessionalName%d", i));
        professional.setFacility(String.format("EntityBuilderProfessionalFacility%d", i));
        professional.setPractice(String.format("EntityBuilderProfessionalPractice%d", i));
        professional.setSpecialist(String.format("EntityBuilderProfessionalSpecialist%d", i));
        professional.setClient(entityBuilder.getClients().get(clientIndex));
        professional.setContactInfo(contactInfo);

        professional = professionalService.create(professional);
        entityBuilder.getProfessionals().add(professional);

        return professional;

    }

    private PatientProfessional createPatientProfessional(EntityBuilder entityBuilder, int professionalIndex, Patient patient) {
        PatientProfessional patientProfessional = new PatientProfessional();
        patientProfessional.setPatient(patient);
        patientProfessional.setProfessional(entityBuilder.getProfessionals().get(professionalIndex));
        patientProfessional.setContactPreference(ContactPreference.EMAIL);

        patientProfessional = patientProfessionalService.create(patientProfessional);
        entityBuilder.getPatientProfessionals().add(patientProfessional);

        return patientProfessional;
    }

    private Advocate createAdvocate(EntityBuilder entityBuilder, Patient patient, int i) {
        ContactInfo contactInfo = new ContactInfo("1234567890",
                "0987654321",
                "5647382910",
                String.format("advocateEmail%d@builder.com", i),
                ContactPreference.EMAIL);
        contactInfo.setTimeZone("America/Denver");

        Advocate advocate = new Advocate();
        advocate.setFirstName(String.format("EntityBuilderAdvocateFName%d", i));
        advocate.setLastName(String.format("EntityBuilderAdvocateLName%d", i));
        advocate.setContactInformation(contactInfo);
        advocate.setPatient(patient);

        advocate = advocateService.create(advocate);
        entityBuilder.getAdvocates().add(advocate);

        return advocate;
    }

    private Medication createMedication(EntityBuilder entityBuilder, int i) {
        Medication medication = new Medication(52000 + new Random().nextInt(99) + i,
                String.format("MedicationName%d", i), "5.0", "MG");

        medicationService.createMedication(medication);

        entityBuilder.getMedications().add(medication);

        return medication;
    }

    private Patient createPatient(EntityBuilder entityBuilder, int clientIndex, int professionalIndex, int i) {
        ContactInfo contactInfo = new ContactInfo("1234567890",
                "0987654321",
                "5647382910",
                String.format("patientemail%d@builder.com", i),
                ContactPreference.EMAIL);
        contactInfo.setTimeZone("America/Chicago");

        Patient patient = new Patient();
        patient.setContactInfo(contactInfo);
        patient.setDob("10/08/1990");
        patient.setLogin(String.format("patientemail%d@builder.com", i));
        patient.setPassword("password");
        patient.setFirstName(String.format("PatientFirstName%d", i));
        patient.setLastName(String.format("PatientLastName%d", i));
        patient.addClient(entityBuilder.getClients().get(clientIndex));

        patient = (Patient) userService.create(patient);

        entityBuilder.getPatients().add(patient);

        return patient;
    }

    private MedicationInstance createMedicationInstance(EntityBuilder entityBuilder, Patient patient, int medicationIndex, int professionalIndex) {
        MedicationInstance medicationInstance = new MedicationInstance(ZonedDateTime.now(), null,
                patient, entityBuilder.getMedications().get(medicationIndex),
                entityBuilder.getProfessionals().get(professionalIndex));

        HashSet<Integer> allDaysOfWeek = new HashSet<>();
        allDaysOfWeek.add(1);
        allDaysOfWeek.add(2);
        allDaysOfWeek.add(3);
        allDaysOfWeek.add(4);
        allDaysOfWeek.add(5);
        allDaysOfWeek.add(6);
        allDaysOfWeek.add(7);

        HashSet<LocalTime> timeTakenList = new HashSet<>();
        timeTakenList.add(LocalTime.of(9, 0));
        timeTakenList.add(LocalTime.of(12, 0));
        timeTakenList.add(LocalTime.of(6, 0));

        MedicationInstanceFrequency medicationInstanceFrequency = new MedicationInstanceFrequency(3l, allDaysOfWeek, 30,1);
        medicationInstanceFrequency.setTakenDaily(true);
        medicationInstanceFrequency.setMedicationInstance(medicationInstance);
        medicationInstanceFrequency.setTimeTakenList(timeTakenList);
        medicationInstanceFrequency.setNumberRefills(3);

        medicationInstance.addMedicationInstanceFrequency(medicationInstanceFrequency);

        medicationInstance = medicationService.createOrUpdateMedicationInstance(medicationInstance);
        entityBuilder.getMedicationInstances().add(medicationInstance);

        return medicationInstance;
    }
}
