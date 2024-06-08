package importador;

import colaboracion.DistribucionDeViandas;
import colaboracion.DonarDinero;
import colaboracion.DonarVianda;
import notificador.Notificador;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import personas.*;
import repositorios.RepositorioColaboradores;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ImportCSV {
    public static void importCSV() throws IOException {
        try (Reader reader = new FileReader("src/main/resources/formatodecsv.csv")) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withDelimiter(';') // el delimitador entre columna y columna es el ;
                    .parse(reader);
            for (CSVRecord record : csvParser) {
                String tipoDoc = record.get(0);
                String documento = record.get(1);
                String nombre = record.get(2);
                String apellido = record.get(3);
                String mail = record.get(4);
                String fecha = record.get(5);
                String formaColaboracion = record.get(6);
                String cantidad = record.get(7);

                if( tipoDoc.length() > 3 || documento.length() > 10 || nombre.length() > 50
                                        || apellido.length() > 50 || mail.length() > 50
                                        || fecha.length() > 10 || formaColaboracion.length() > 22
                                        || cantidad.length() > 7 ){
                    System.out.println(" ---------------------- ");
                    System.out.println("Error de carga en archivo CSV en linea: " + record.getRecordNumber());
                    System.out.println("Uno o mas campos exceden la longitud permitida");
                    System.out.println(" ---------------------- ");
                    continue;
                }

                TipoDocumento tipoDocumento = null;
                switch (tipoDoc) {
                    case "DNI":
                        tipoDocumento = TipoDocumento.DNI;
                        break;
                    case "LC":
                        tipoDocumento = TipoDocumento.LC;
                        break;
                    case "LE":
                        tipoDocumento = TipoDocumento.LE;
                        break;
                    default:
                        System.out.println(" ---------------------- ");
                        System.out.println("Error de carga en archivo CSV en linea: " + record.getRecordNumber());
                        System.out.println("Tipo de documento no valido");
                        System.out.println(" ---------------------- ");
                        continue; // busca el siguiente registro
                }

                String identificadorUnico = tipoDocumento + documento;

                RepositorioColaboradores repoColaboradores = RepositorioColaboradores.getInstancia();
                Colaborador colaborador = repoColaboradores.existeColaborador(identificadorUnico);

                if (colaborador == null){ // NO EXISTE COLABORADOR
                    MedioDeContacto medioDeContacto = new MedioDeContacto(TipoMedioDeContacto.MAIL, mail);
                    ArrayList<MedioDeContacto> mediosDeContacto = new ArrayList<>();
                    mediosDeContacto.add(medioDeContacto);

                    PersonaHumana persona = new PersonaHumana(tipoDocumento, documento, nombre, apellido, mail, mediosDeContacto);

                    repoColaboradores.agregar(persona);

                    // TODO
                    // NOTIFICAR

                }



                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Va ese formato para la fecha que levanta del CSV
                LocalDateTime fechaColaboracionAux = LocalDate.parse(fecha, formatter).atStartOfDay(); // Levanta este formato dd/MM/yyyy y lo convierte a LocalDateTime agregandole la hora 00:00:00

                //TODO
                switch (formaColaboracion) {
                    case "DINERO":
                        DonarDinero donacionDinero = new DonarDinero(colaborador.getTipo(), Double.parseDouble(cantidad), fechaColaboracionAux); //cantidad es un Double
                        colaborador.colaborar(donacionDinero);
                        break;
                    case "DONACION_VIANDAS":
                        // que hacemos con la cantidad?
                        DonarVianda donacionVianda = new DonarVianda(colaborador.getTipo(), fechaColaboracionAux);
                        colaborador.colaborar(donacionVianda);
                        break;
                    case "REDISTRIBUCION_VIANDAS":
                        DistribucionDeViandas donacionDistribucion = new DistribucionDeViandas(colaborador.getTipo(), fechaColaboracionAux, Integer.parseInt(cantidad));
                        colaborador.colaborar(donacionDistribucion);
                        break;
                    case "ENTREGA_TARJETAS":
                        break;
                    default:
                        System.out.println("Error de carga en archivo CSV en linea: " + record.getRecordNumber());
                        System.out.println("Forma de colaboracion no valida");
                        continue;
                }



                System.out.println("Record No - " + record.getRecordNumber());
                System.out.println("TipoDoc: " + tipoDoc);
                System.out.println("Documento: " + documento);
                System.out.println("Nombre: " + nombre);
                System.out.println("Apellido: " + apellido);
                System.out.println("mail: " + mail);
                System.out.println("fecha colaboracion: " + fecha);
                System.out.println("forma de colaboracion: " + formaColaboracion);
                System.out.println("cantidad: " + cantidad);
            }
        }
    }

    public static PersonaHumana buscarPersona(String documento, String tipoDoc){
        //TODO
        //Buscar persona en la lista de personas o Base de datos del sistema??
        //Si la encuentra, retornarla
        //Si no la encuentra, retornar null
        return null;
    }
}