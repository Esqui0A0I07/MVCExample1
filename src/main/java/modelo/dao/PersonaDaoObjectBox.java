package modelo.dao;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import modelo.entity.MyObjectBox;
import modelo.entity.PersonaEntity;
import modelo.vo.PersonaVo;

import javax.swing.*;
import java.io.File;

public class PersonaDaoObjectBox implements IPersonaDao {

    private final BoxStore boxStore;
    private final Box<PersonaEntity> personaBox;

    public PersonaDaoObjectBox(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        this.boxStore = MyObjectBox.builder()
                .directory(directorio)
                .build();
        this.personaBox = boxStore.boxFor(PersonaEntity.class);
        System.out.println("Conexión a ObjectBox OK (" + rutaDirectorio + ")");
    }

    @Override
    public void registrarPersona(PersonaVo miPersona) {
        try {
            long id = miPersona.getIdPersona();
            if (personaBox.get(id) != null) {
                JOptionPane.showMessageDialog(null, "No se Registró: ya existe una persona con ese código",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            personaBox.put(PersonaEntity.desdeVo(miPersona));
            JOptionPane.showMessageDialog(null, "Se ha registrado Exitosamente",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "No se Registró");
        }
    }

    @Override
    public PersonaVo buscarPersona(int codigo) {
        try {
            PersonaEntity entidad = personaBox.get((long) codigo);
            return entidad != null ? entidad.aVo() : null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar en ObjectBox");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void modificarPersona(PersonaVo miPersona) {
        try {
            long id = miPersona.getIdPersona();
            if (personaBox.get(id) == null) {
                JOptionPane.showMessageDialog(null, "No existe la persona a modificar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            personaBox.put(PersonaEntity.desdeVo(miPersona));
            JOptionPane.showMessageDialog(null, "Se ha Modificado Correctamente",
                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al Modificar",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void eliminarPersona(String codigo) {
        try {
            long id = Long.parseLong(codigo.trim());
            boolean eliminado = personaBox.remove(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Se ha Eliminado Correctamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro para eliminar",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "No se Eliminó");
        }
    }

    @Override
    public void cerrar() {
        if (boxStore != null && !boxStore.isClosed()) {
            boxStore.close();
        }
    }
}