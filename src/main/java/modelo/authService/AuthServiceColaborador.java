package modelo.authService;

import modelo.excepciones.ExcepcionValidacion;
import modelo.personas.TipoDocumento;
import persistencia.RepositorioColaboradores;

public class AuthServiceColaborador {

    private static RepositorioColaboradores repoColab = RepositorioColaboradores.getInstancia();

    public static void registrarColaboradorFisico(TipoDocumento tipoDoc, String nroDoc, String nombre, String apellido, String mail, String telefono, String direccion, String fechaNacimiento) {
        if (repoColab.existePersonaFisica(nroDoc, tipoDoc) != null) {
            throw new ExcepcionValidacion("El colaborador ya existe");
        }
        repoColab.registrarColaboradorFisico(tipoDoc, nroDoc, nombre, apellido, mail, telefono, direccion, fechaNacimiento);

    }

    public static void registrarColaboradorJuridico(String razonSocial, String nombreFantasia, String mail, String telefono, String direccion) {
//        if (repoColab.existePersonaJuridica(razonSocial) != null) {
//            throw new ExcepcionValidacion("El colaborador ya existe");
//        }
//        repoColab.registrarColaboradorJuridico(razonSocial, nombreFantasia, mail, telefono, direccion);

        // USA ESTA EXCEPCION PARA CUALQUIER VALIDACION QUE HAGAS ExcepcionValidacion
    }


}