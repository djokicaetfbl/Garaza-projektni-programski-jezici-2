/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo.interfejs;

import vozilo.prioritet.Prioritet;
/**
 *
 * @author djord
 */
public interface VatrogasnoVoziloInterface extends SpecijalnoVoziloInterface {
    final Prioritet prioritet = Prioritet.SREDNJI;
}
