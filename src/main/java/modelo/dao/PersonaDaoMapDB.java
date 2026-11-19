package modelo.dao;

import modelo.vo.PersonaVo;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import javax.swing.*;
import java.io.File;

public class PersonaDaoMapDB implements IPersonaDao {

    private final DB db;
    private final HTreeMap<Integer, Object> mapaPersonas;
    private final boolean transaccionesHabilitadas;

    public PersonaDaoMapDB(String rutaArchivo, String nombreMapa, boolean transaccionesHabilitadas) {
        this.transaccionesHabilitadas = transaccionesHabilitadas;

        File archivo = new File(rutaArchivo);
        if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
            archivo.getParentFile().mkdirs();
        }

        DBMaker.Maker maker = DBMaker.fileDB(archivo).closeOnJvmShutdown();
        if (transaccionesHabilitadas) {
            maker.transactionEnable();
        }

        this.db = maker.make();
        this.mapaPersonas = db.hashMap(nombreMapa, Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        System.out.println("Conexión a MapDB OK (" + rutaArchivo + ")");
    }

    private void confirmarCambios() {
        if (transaccionesHabilitadas) {
            db.commit();
        }
    }

    @Override
    public void registrarPersona(PersonaVo miPersona) {
        try {
            if (mapaPersonas.containsKey(miPersona.getIdPersona())) {
                JOptionPane.showMessageDialog(null, "No se Registró: ya existe una persona con ese código",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mapaPersonas.put(miPersona.getIdPersona(), miPersona);
            confirmarCambios();
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
            Object resultado = mapaPersonas.get(codigo);
            if (resultado instanceof PersonaVo) {
                return (PersonaVo) resultado;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar en MapDB");
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void modificarPersona(PersonaVo miPersona) {
        try {
            if (!mapaPersonas.containsKey(miPersona.getIdPersona())) {
                JOptionPane.showMessageDialog(null, "No existe la persona a modificar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mapaPersonas.put(miPersona.getIdPersona(), miPersona);
            confirmarCambios();
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
            int id = Integer.parseInt(codigo.trim());
            Object eliminado = mapaPersonas.remove(id);
            if (eliminado != null) {
                confirmarCambios();
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
        if (db != null && !db.isClosed()) {
            db.close();
        }
    }
}