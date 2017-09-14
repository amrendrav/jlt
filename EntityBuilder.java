package com.tii.php.util.builder;

import com.tii.php.entity.*;
import com.tii.php.entity.medlibrary.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbertiaux on 9/20/16.
 */
public class EntityBuilder {

    private boolean initialized = false;

    private List<Patient> patients = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private List<Professional> professionals = new ArrayList<>();
    private List<PatientProfessional> patientProfessionals = new ArrayList<>();
    private List<Advocate> advocates = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();
    private List<MedicationInstance> medicationInstances = new ArrayList<>();

    private int numPatients = 1;
    private int numClients = 1;
    private int numProfessionals = 1;
    private int numPatientProfessionals = 1;
    private int numAdvocates = 1;
    private int numMedications = 1;
    private int numMedicationInstances = 1;

    /**
     * Sets the number of patients to create. The default is 1.
     *
     * @param numPatients
     * @return
     */
    public EntityBuilder withPatients(int numPatients) {
        this.numPatients = numPatients;
        return this;
    }

    /**
     * Sets the number of clients to create. The default is 1. If more than one client is created, then the client list
     * will be cycled through for each time a client is needed.
     *
     * @param numClients
     * @return
     */
    public EntityBuilder withClients(int numClients) {
        this.numClients = numClients;
        return this;
    }

    /**
     * Sets the number of professionals to create. The default is 1. If more than one professional is created, then the professional list
     * will be cycled through for each time a professional is needed. Each patient gets one professional for now.
     *
     * @param numProfessionals
     * @return
     */
    public EntityBuilder withProfessionals(int numProfessionals) {
        this.numProfessionals = numProfessionals;
        return this;
    }

    /**
     * Sets the number of patientProfessional to create. The default is 1. If more than one patientProfessional is created, then the patientProfessional list
     * will be cycled through for each time a patientProfessional is needed. Each patient gets one patientProfessional for now.
     *
     * @param numPatientProfessionals
     * @return
     */
    public EntityBuilder withPatientProfessionals(int numPatientProfessionals) {
        this.numPatientProfessionals = numPatientProfessionals;
        return this;
    }

    /**
     * Sets the number of advocates to create. The default is 1. If more than one advocate is created, then the advocate list
     * will be cycled through for each time a advocate is needed. Each patient gets one advocate for now.
     *
     * @param numAdvocates
     * @return
     */
    public EntityBuilder withAdvocates(int numAdvocates) {
        this.numAdvocates = numAdvocates;
        return this;
    }

    /**
     * Sets the number of medications to create. The default is 1. If more than one medication is created, then the medication list
     * will be cycled through for each time a medication is needed.
     *
     * @param numMedications
     * @return
     */
    public EntityBuilder withMedications(int numMedications) {
        this.numMedications = numMedications;
        return this;
    }

    /**
     * Sets the number of daily medication instances to create per patient. The default is 1. All MedicationInstances
     * will be created with the time taken slots at 9:00 a.m., 12:00 p.m., and 6:00 p.m.
     *
     * @param numMedicationInstances
     * @return
     */
    public EntityBuilder withDailyMedicationInstancesPerPatient(int numMedicationInstances) {
        this.numMedicationInstances = numMedicationInstances;
        return this;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public List<MedicationInstance> getMedicationInstances() {
        return medicationInstances;
    }

    protected int getNumPatients() {
        return numPatients;
    }

    protected int getNumClients() {
        return numClients;
    }

    protected int getNumProfessionals() {
        return numProfessionals;
    }

    protected int getNumMedications() {
        return numMedications;
    }

    protected int getNumMedicationInstances() {
        return numMedicationInstances;
    }

    public List<Advocate> getAdvocates() {
        return advocates;
    }

    public void setAdvocates(List<Advocate> advocates) {
        this.advocates = advocates;
    }

    protected int getNumAdvocates() {
        return numAdvocates;
    }

    protected void setNumAdvocates(int numAdvocates) {
        this.numAdvocates = numAdvocates;
    }

    public List<PatientProfessional> getPatientProfessionals() {
        return patientProfessionals;
    }

    public void setPatientProfessionals(List<PatientProfessional> patientProfessionals) {
        this.patientProfessionals = patientProfessionals;
    }

    public int getNumPatientProfessionals() {
        return numPatientProfessionals;
    }

    public void setNumPatientProfessionals(int numPatientProfessionals) {
        this.numPatientProfessionals = numPatientProfessionals;
    }
}
