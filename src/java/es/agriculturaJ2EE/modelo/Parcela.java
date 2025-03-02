/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE.modelo;

public class Parcela {
    private String idParcela;
    private String idCatastro;
    private String numeroParcela;

    // Getters y setters
    public String getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(String idParcela) {
        this.idParcela = idParcela;
    }

    public String getIdCatastro() {
        return idCatastro;
    }
    public void setIdCatastro(String idCatastro) {
        this.idCatastro = idCatastro;
    }
    public String getNumeroParcela() {
        return numeroParcela;
    }
    public void setNumeroParcela(String numeroParcela) {
        this.numeroParcela = numeroParcela;
    }
}
