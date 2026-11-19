package modelo;

import controlador.Coordinador;
import modelo.dao.IPersonaDao;
import modelo.vo.PersonaVo;

import javax.swing.*;

public class Logica {

    private Coordinador miCoordinador;
    private IPersonaDao personaDao;
    public static boolean consultaPersona = false;
    public static boolean modificaPersona = false;

    public void setCoordinador(Coordinador miCoordinador) {
        this.miCoordinador = miCoordinador;
    }

    public void setPersonaDao(IPersonaDao personaDao) {
        this.personaDao = personaDao;
    }

    public void validarRegistro(PersonaVo miPersona) {
        if (miPersona.getIdPersona() > 99) {
            personaDao.registrarPersona(miPersona);
        } else {
            JOptionPane.showMessageDialog(null, "El documento de la persona debe ser mas de 3 digitos",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public PersonaVo validarConsulta(String codigoPersona) {
        try {
            int codigo = Integer.parseInt(codigoPersona);
            if (codigo > 99) {
                consultaPersona = true;
                return personaDao.buscarPersona(codigo);
            } else {
                JOptionPane.showMessageDialog(null, "El documento de la persona debe ser mas de 3 digitos",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                consultaPersona = false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un dato numerico",
                    "Error", JOptionPane.ERROR_MESSAGE);
            consultaPersona = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se ha presentado un Error",
                    "Error", JOptionPane.ERROR_MESSAGE);
            consultaPersona = false;
        }

        return null;
    }

    public void validarModificacion(PersonaVo miPersona) {
        if (miPersona.getNombrePersona().length() > 5) {
            personaDao.modificarPersona(miPersona);
            modificaPersona = true;
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de la persona debe ser mayor a 5 digitos",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            modificaPersona = false;
        }
    }

    public void validarEliminacion(String codigo) {
        personaDao.eliminarPersona(codigo);
    }
}