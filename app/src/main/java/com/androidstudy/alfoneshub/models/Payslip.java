package com.androidstudy.alfoneshub.models;

public class Payslip {
    String id, payslip_id, admin_id, name, employee_no, gross_salary, nssf_contibution, taxable_pay, personal_relief, insurance_relief, paye, nhif_contribution, net_pay, tax_rate, month, year, created_at, updated_at;

    public Payslip() {
    }

    public Payslip(String id, String payslip_id, String admin_id, String name, String employee_no, String gross_salary, String nssf_contibution, String taxable_pay, String personal_relief, String insurance_relief, String paye, String nhif_contribution, String net_pay, String tax_rate, String month, String year, String created_at, String updated_at) {
        this.id = id;
        this.payslip_id = payslip_id;
        this.admin_id = admin_id;
        this.name = name;
        this.employee_no = employee_no;
        this.gross_salary = gross_salary;
        this.nssf_contibution = nssf_contibution;
        this.taxable_pay = taxable_pay;
        this.personal_relief = personal_relief;
        this.insurance_relief = insurance_relief;
        this.paye = paye;
        this.nhif_contribution = nhif_contribution;
        this.net_pay = net_pay;
        this.tax_rate = tax_rate;
        this.month = month;
        this.year = year;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayslip_id() {
        return payslip_id;
    }

    public void setPayslip_id(String payslip_id) {
        this.payslip_id = payslip_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getGross_salary() {
        return gross_salary;
    }

    public void setGross_salary(String gross_salary) {
        this.gross_salary = gross_salary;
    }

    public String getNssf_contibution() {
        return nssf_contibution;
    }

    public void setNssf_contibution(String nssf_contibution) {
        this.nssf_contibution = nssf_contibution;
    }

    public String getTaxable_pay() {
        return taxable_pay;
    }

    public void setTaxable_pay(String taxable_pay) {
        this.taxable_pay = taxable_pay;
    }

    public String getPersonal_relief() {
        return personal_relief;
    }

    public void setPersonal_relief(String personal_relief) {
        this.personal_relief = personal_relief;
    }

    public String getInsurance_relief() {
        return insurance_relief;
    }

    public void setInsurance_relief(String insurance_relief) {
        this.insurance_relief = insurance_relief;
    }

    public String getPaye() {
        return paye;
    }

    public void setPaye(String paye) {
        this.paye = paye;
    }

    public String getNhif_contribution() {
        return nhif_contribution;
    }

    public void setNhif_contribution(String nhif_contribution) {
        this.nhif_contribution = nhif_contribution;
    }

    public String getNet_pay() {
        return net_pay;
    }

    public void setNet_pay(String net_pay) {
        this.net_pay = net_pay;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
