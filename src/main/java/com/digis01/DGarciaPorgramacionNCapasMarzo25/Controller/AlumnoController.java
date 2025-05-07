package com.digis01.DGarciaPorgramacionNCapasMarzo25.Controller;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Alumno;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.AlumnoDireccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Colonia;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Direccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Estado;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.ML.Municipio;
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
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    String urlBase = "http://localhost:8081/";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/CargaMasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("/CargaMasiva")
    public String CargaMasiva(@RequestParam MultipartFile archivo, Model model, HttpSession session) {

        try {
            //Guardarlo en un punto del sistema
            if (archivo != null && !archivo.isEmpty()) { //El archivo no sea nulo ni esté vacío

                //Body 
                ByteArrayResource byteArrayResource = new ByteArrayResource(archivo.getBytes()) {
                    @Override
                    public String getFilename() {
                        return archivo.getOriginalFilename();
                    }
                };

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("archivo", byteArrayResource);

                //Headers
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                //Entidad de la petición
                HttpEntity<MultiValueMap<String, Object>> httpEntity
                        = new HttpEntity<>(body, httpHeaders);

                ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                        urlBase + "alumnoapi/CargaMasiva",
                        HttpMethod.POST,
                        httpEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                });

                //¿Dónde viene mi lista de Errores?
                //(boolean) responseEntity.getBody().get("correct") == true
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    model.addAttribute("correcto", true);
                    session.setAttribute("urlFile", responseEntity.getBody().get("object"));

                } else {

                    if (responseEntity.getStatusCode().is4xxClientError()) {
                        model.addAttribute("listaErrores", (String) responseEntity.getBody().get("objects"));
                    }

                }

            }
        } catch (Exception ex) {
            return "redirect:/Alumno/CargaMasiva";
        }

        return "CargaMasiva";
    }

    @GetMapping("/CargaMasiva/Procesar")
    public String Procesar(HttpSession session) {

        String absolutePath = session.getAttribute("urlFile").toString();

        ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "alumnoapi/CargaMasiva/Procesar",
                HttpMethod.POST,
                new HttpEntity<>(absolutePath),
                new ParameterizedTypeReference<Result>() {
        });

        if (responseEntity.getBody().correct) {
            //Validaciones
        }

        if (responseEntity.getStatusCode().equals(200)) {
            //Validaciones
        }

        return "/CargaMasiva";
    }

    @GetMapping
    public String Index(Model model) {

        //        Result result = alumnoDAOImplementation.GetAll();
//        Result resultJPA = alumnoDAOImplementation.GetAllJPA();
//        Result resultSemestre = SemestreDAOImplementation.GetAll();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<AlumnoDireccion>> responseEntity = restTemplate.exchange("http://localhost:8081/alumnoapi",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<AlumnoDireccion>>() {
        });

        Result response = responseEntity.getBody();

        Alumno alumnoBusqueda = new Alumno();
        alumnoBusqueda.Semestre = new Semestre();

        model.addAttribute("alumnoBusqueda", alumnoBusqueda);
//        model.addAttribute("semestres", resultSemestre.object);
        model.addAttribute("listaAlumnos", response.objects);

        return "AlumnoIndex";
    }

    @GetMapping("/deleteAlumno/{IdAlumno}")
    public String DeleteAlumno(@PathVariable int IdAlumno) {

        try {

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "alumnoapi/delete/" + IdAlumno,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result>() {
            });
            Result result = responseEntity.getBody();

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        return "redirect:/Alumno";
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
            alumnoDireccion.Direccion.Colonia.Municipio = new Municipio();
            alumnoDireccion.Direccion.Colonia.Municipio.Estado = new Estado();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Result<List<Semestre>>> response = restTemplate.exchange("http://localhost:8081/semestreapi",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Semestre>>>() {
            });

            ResponseEntity<Result<List<Estado>>> responseEstado = restTemplate.exchange("http://localhost:8081/estadoapi",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Estado>>>() {
            });

            model.addAttribute("semestres", response.getBody().object);
            model.addAttribute("alumnoDireccion", alumnoDireccion);
            model.addAttribute("estados", responseEstado.getBody().object);
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
                    entity,
                    new ParameterizedTypeReference<Result>() {
            });
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
