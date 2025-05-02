package com.digis01.DGarciaPorgramacionNCapasMarzo25.Controller;


import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Alumno;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.AlumnoDireccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Colonia;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Direccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Result;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.ResultFile;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Semestre;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Alumno")
public class AlumnoController {

//   @GetMapping("/CargaMasiva")
//    public String CargaMasiva() {
//        return "CargaMasiva";
//    }
//
//    @PostMapping("/CargaMasiva")
//    public String CargaMasiva(@RequestParam MultipartFile archivo, Model model, HttpSession session) {
//
//        try {
//            //Guardarlo en un punto del sistema
//            if (archivo != null && !archivo.isEmpty()) { //El archivo no sea nulo ni esté vacío
//
//                //"NombreArchivo.txt"
//                //String[] arreglo = {"NombreArchivo", "txt"};
//                String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];
//
//                String root = System.getProperty("user.dir"); //Obtener direccion del proyecto en el equipo
//                String path = "src/main/resources/static/archivos"; //Path relativo dentro del proyecto
//                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//                String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();
//                archivo.transferTo(new File(absolutePath));
//
//                //Leer el archivo
//                List<AlumnoDireccion> listaAlumnos = new ArrayList();
//                if (tipoArchivo.equals("txt")) {
//                    listaAlumnos = LecturaArchivoTXT(new File(absolutePath)); //método para leer la lista
//                } else {
//                    listaAlumnos = LecturaArchivoExcel(new File(absolutePath));
//                }
//
//                //Validar el archivo
//                List<ResultFile> listaErrores = ValidarArchivo(listaAlumnos);
//
//                if (listaErrores.isEmpty()) {
//                    //Proceso mi archivo
//                    session.setAttribute("urlFile", absolutePath);
//                    session.setAttribute("tipoArchivo", tipoArchivo);
//                    model.addAttribute("listaErrores", listaErrores);
//                } else {
//                    //Mando mis errores
//                    model.addAttribute("listaErrores", listaErrores);
//                }
//
//            }
//        } catch (Exception ex) {
//            return "redirect:/Alumno/CargaMasiva";
//        }
//
//        return "CargaMasiva";
//    }
//
//    public List<AlumnoDireccion> LecturaArchivoTXT(File archivo) {
//        List<AlumnoDireccion> listaAlumnos = new ArrayList<>();
//
//        try (FileReader fileReader = new FileReader(archivo); BufferedReader bufferedReader = new BufferedReader(fileReader);) {
//
//            String linea;
//
//            while ((linea = bufferedReader.readLine()) != null) {
//                String[] campos = linea.split("\\|");
//
//                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
//                alumnoDireccion.Alumno = new Alumno();
//                alumnoDireccion.Alumno.setNombre(campos[0]);
//                alumnoDireccion.Alumno.setApellidoPaterno(campos[1]);
//                alumnoDireccion.Alumno.setApellidoMaterno(campos[2]);
//                alumnoDireccion.Alumno.setUsername(campos[3]);
//                alumnoDireccion.Alumno.setEmail(campos[4]);
//                //Darle formato a la fecha de nacimiento
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Dar formato a la fecha
//                alumnoDireccion.Alumno.setFechaNacimiento(formatter.parse(campos[5]));
//                alumnoDireccion.Alumno.setStatus(Integer.parseInt(campos[6]));
//                alumnoDireccion.Alumno.setImagen(null);
//                alumnoDireccion.Alumno.Semestre = new Semestre();
//                alumnoDireccion.Alumno.Semestre.setIdSemestre(Integer.parseInt(campos[7]));
//
//                alumnoDireccion.Direccion = new Direccion();
//                alumnoDireccion.Direccion.setCalle(campos[8]);
//                alumnoDireccion.Direccion.setNumeroExterior(campos[9]);
//                alumnoDireccion.Direccion.setNumeroInterior(campos[10]);
//
//                alumnoDireccion.Direccion.Colonia = new Colonia();
//                alumnoDireccion.Direccion.Colonia.setIdColonia(Integer.parseInt(campos[11]));
//
//                listaAlumnos.add(alumnoDireccion);
//            }
//
//        } catch (Exception ex) {
//            listaAlumnos = null;
//        }
//
//        return listaAlumnos;
//    }
//
//    public List<AlumnoDireccion> LecturaArchivoExcel(File archivo) {
//        List<AlumnoDireccion> listaAlumnos = new ArrayList<>();
//        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo);) {
//            for (Sheet sheet : workbook) {
//
//                for (Row row : sheet) {
//
//                    AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
//                    alumnoDireccion.Alumno = new Alumno();
//                    alumnoDireccion.Alumno.setNombre(row.getCell(0).toString());
//                    alumnoDireccion.Alumno.setApellidoPaterno(row.getCell(1).toString());
//                    alumnoDireccion.Alumno.setApellidoMaterno(row.getCell(2).toString());
//                    alumnoDireccion.Alumno.setEmail(row.getCell(3).toString());
//                    alumnoDireccion.Alumno.Semestre = new Semestre();
//                    alumnoDireccion.Alumno.Semestre.setIdSemestre(Integer.parseInt(row.getCell(4).toString()));
//                    alumnoDireccion.Alumno.setStatus(row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0 );
//                    
//                }
//                
//            }
//        } catch (Exception ex) {
//            System.out.println("Error al abrir el archivo");
//        }
//
//        return listaAlumnos;
//    }
//
//    public List<ResultFile> ValidarArchivo(List<AlumnoDireccion> listaAlumnos) {
//        List<ResultFile> listaErrores = new ArrayList<>();
//        
//        
//        
//        
//        
//
//        if (listaAlumnos == null) {
//            listaErrores.add(new ResultFile(0, "La lista es nula", "La lista es nula"));
//        } else if (listaAlumnos.isEmpty()) {
//            listaErrores.add(new ResultFile(0, "La lista está vacía", "La lista está vacía"));
//        } else {
//            int fila = 1;
//            for (AlumnoDireccion alumnoDireccion : listaAlumnos) {
//                if (alumnoDireccion.Alumno.getNombre() == null || alumnoDireccion.Alumno.getNombre().equals("")) {
//                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getNombre(), "El nombre es un campo oligatorio"));
//                }
//
//                if (alumnoDireccion.Alumno.getApellidoPaterno() == null || alumnoDireccion.Alumno.getApellidoPaterno().equals("")) {
//                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getApellidoPaterno(), "El Apellido Paterno es un campo oligatorio"));
//                }
//
//                if (alumnoDireccion.Alumno.getUsername() == null || alumnoDireccion.Alumno.getUsername().equals("")) {
//                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getApellidoPaterno(), "El Username es un campo oligatorio"));
//                }
//                fila++;
//            }
//        }
//        return listaErrores;
//    }
//    
//    @GetMapping("/CargaMasiva/Procesar")
//    public String Procesar(HttpSession session) {
//        
//        String absolutePath = session.getAttribute("urlFile").toString();
//        String tipoArchivo = session.getAttribute("tipoArchivo").toString();
//        List<AlumnoDireccion> listaAlumnos = new ArrayList<>();
//        
//        if (tipoArchivo.equals("txt")) {
//            listaAlumnos = LecturaArchivoTXT(new File(absolutePath));
//        } else {
//            listaAlumnos = LecturaArchivoExcel(new File(absolutePath));
//        }
//        
//        for (AlumnoDireccion alumnoDireccion : listaAlumnos) {
//            alumnoDAOImplementation.Add(alumnoDireccion);
//        }
//        
//        return "/CargaMasiva";
//    }
//    
    @GetMapping
    public String Index(Model model) {

        //        Result result = alumnoDAOImplementation.GetAll();
//        Result resultJPA = alumnoDAOImplementation.GetAllJPA();
//        Result resultSemestre = SemestreDAOImplementation.GetAll();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<AlumnoDireccion>> responseEntity = restTemplate.exchange("http://localhost:8081/alumnoapi", 
                HttpMethod.GET, 
                HttpEntity.EMPTY, 
                new ParameterizedTypeReference<Result<AlumnoDireccion>>(){});
        
        
        Result response = responseEntity.getBody();
        
        
        Alumno alumnoBusqueda = new Alumno();
        alumnoBusqueda.Semestre = new Semestre();

        model.addAttribute("alumnoBusqueda", alumnoBusqueda);
//        model.addAttribute("semestres", resultSemestre.object);
        model.addAttribute("listaAlumnos", response.objects);

        return "AlumnoIndex";
    }
//
//    @PostMapping("/GetAllDinamico")
//    public String BusquedaDinamica(@ModelAttribute Alumno alumno, Model model) {
//
//        Result result = alumnoDAOImplementation.GetAllDinamico(alumno);
//        Result resultSemestre = SemestreDAOImplementation.GetAll();
//        
//        model.addAttribute("semestres", resultSemestre.object);
//        model.addAttribute("listaAlumnos", result.objects);
//        model.addAttribute("alumnoBusqueda", alumno);
//
//        return "AlumnoIndex";
//    }
//
    @GetMapping("Form/{IdAlumno}")
    public String Form(@PathVariable int IdAlumno, Model model) {
        if (IdAlumno == 0) { // Agregar
            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
            alumnoDireccion.Alumno = new Alumno();
            alumnoDireccion.Alumno.Semestre = new Semestre();
            alumnoDireccion.Direccion = new Direccion();
            alumnoDireccion.Direccion.Colonia = new Colonia();

//            model.addAttribute("semestres", SemestreDAOImplementation.GetAll().object);
//            model.addAttribute("alumnoDireccion", alumnoDireccion);
//            model.addAttribute("estados", estadoDAOImplementation.GetAll().correct ? estadoDAOImplementation.GetAll().objects : null);
            return "AlumnoForm";
        } else { // Editar
            System.out.println("Voy a editar");
//            Result result = alumnoDAOImplementation.direccionesByIdUsuario(IdAlumno);
//            model.addAttribute("alumnoDirecciones", result.object);
            return "AlumnoDetail";
        }
    }
//
//    @GetMapping("/formEditable")
//    public String FormEditable(Model model, @RequestParam int IdAlumno, @RequestParam(required = false) Integer IdDireccion) {
//
//        if (IdDireccion == null) { //Editar Alumno
//            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
////            alumnoDireccion.Alumno.setIdAlumno(1);
//
//            alumnoDireccion = (AlumnoDireccion) alumnoDAOImplementation.GetById(IdAlumno).object;
//            alumnoDireccion.Direccion = new Direccion();
//            alumnoDireccion.Direccion.setIdDireccion(-1);
//            model.addAttribute("alumnoDireccion", alumnoDireccion);
//
//            model.addAttribute("semestres", SemestreDAOImplementation.GetAll().object);
//        } else if (IdDireccion == 0) { //Agregar dirección
//            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
//            alumnoDireccion.Alumno = new Alumno();
//            alumnoDireccion.Alumno.setIdAlumno(1);
//            alumnoDireccion.Direccion = new Direccion();
//            alumnoDireccion.Direccion.setIdDireccion(0);
//            model.addAttribute("alumnoDireccion", alumnoDireccion);
//            model.addAttribute("estados", estadoDAOImplementation.GetAll().correct ? estadoDAOImplementation.GetAll().objects : null);
//        } else { //Editar dirección
//            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
//            alumnoDireccion.Alumno = new Alumno();
//            alumnoDireccion.Alumno.setIdAlumno(IdAlumno);
//            alumnoDireccion.Direccion = new Direccion();
//            alumnoDireccion.Direccion.setIdDireccion(IdDireccion);
//
//            alumnoDireccion.Direccion = (Direccion) direccionDAOImplementation.GetByIdDireccion(IdDireccion).object;
//
//            model.addAttribute("alumnoDireccion", alumnoDireccion);
//            model.addAttribute("estados", estadoDAOImplementation.GetAll().correct ? estadoDAOImplementation.GetAll().objects : null);
//            // model.addAttribut("municipios", municipioDAOIMplementation.MunicipiosByIdEstado)
//        }   // model.addAttribute("colonias", coloniaDAOIMplementation.ColoniasByIdMunicipios)
//
//        return "AlumnoForm";
//    }
//
    @PostMapping("Form")
    public String Form(@Valid @ModelAttribute AlumnoDireccion alumnoDireccion, BindingResult BindingResult, @RequestParam MultipartFile imagenFile, Model model) {

        try {
            if (!imagenFile.isEmpty()) {
                byte[] bytes = imagenFile.getBytes();
                String imgBase64 = Base64.getEncoder().encodeToString(bytes);
                alumnoDireccion.Alumno.setImagen(imgBase64);
            }
        } catch (Exception ex) {
            //Regresar al Form con la información que ya estaba
        }
        
        RestTemplate restTemplate = new RestTemplate();

        if (alumnoDireccion.Alumno.getIdAlumno() == 0) { //Agregar
            //Logica para consumir DAO para agregar un nuevo usuario
//            alumnoDireccion.Alumno.Semestre = new Semestre();
//            alumnoDireccion.Alumno.Semestre.setIdSemestre(10);
            System.out.println("Estoy agregando un nuevo usuario y direccion");
            alumnoDireccion.Alumno.setFechaNacimiento(new Date());
//            alumnoDAOImplementation.Add(alumnoDireccion);

            HttpEntity<AlumnoDireccion> entity = new HttpEntity<>(alumnoDireccion);
            restTemplate.exchange("endpointAdd",
                    HttpMethod.POST,
                    entity ,
                    new ParameterizedTypeReference<Result>(){});
        } else {
            if (alumnoDireccion.Direccion.getIdDireccion() == -1) { //Editar usuario
//                alumnoDAOImplementation.Update(alumnoDireccion.Alumno);
                System.out.println("Estoy actualizando un usuario");
            } else if (alumnoDireccion.Direccion.getIdDireccion() == 0) { //Agregar direccion
                //alumnoDAOImplementation.AddDireccion(alumnoDireccion);
                System.out.println("Estoy agregando direccion");
            } else { //Editar direccion
                //alumnoDAOImplementation.UpdateDireccion(alumnoDireccion);
                System.out.println("Estoy actualizando direccion");
            }
        }

        return "redirect:/Alumno";
    }
//
//    @GetMapping("MunicipioByIdEstado/{IdEstado}")
//    @ResponseBody
//    public Result MunicipioByIdEstado(@PathVariable int IdEstado) {
//        Result result = municipioDAOImplementation.MunicipioByIdEstado(IdEstado);
//
//        return result;
//    }
//
//    @GetMapping("ColoniaByIdMunicipio/{IdMunicipio}")
//    @ResponseBody
//    public Result ColoniaByIdMunicipio(@PathVariable int IdMunicipio) {
//        Result result = coloniaDAOImplementation.ColoniaByIdMunicipio(IdMunicipio);
//
//        return result;
//    }
}
