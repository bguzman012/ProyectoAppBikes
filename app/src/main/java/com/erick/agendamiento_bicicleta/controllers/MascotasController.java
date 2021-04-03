package com.erick.agendamiento_bicicleta.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.erick.agendamiento_bicicleta.AyudanteBaseDeDatos;
import com.erick.agendamiento_bicicleta.modelos.Citas;
import com.erick.agendamiento_bicicleta.modelos.Mascota;
import com.erick.agendamiento_bicicleta.modelos.User;


public class MascotasController {
    private AyudanteBaseDeDatos ayudanteBaseDeDatos;
    private String NOMBRE_TABLA = "mascotas";
    private String usuarios = "users";
    private String citas = "citas";

    public MascotasController(Context contexto) {
        ayudanteBaseDeDatos = new AyudanteBaseDeDatos(contexto);
    }


    public int eliminarMascota(Mascota mascota) {

        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(mascota.getId())};
        return baseDeDatos.delete(NOMBRE_TABLA, "id = ?", argumentos);
    }

    public long nuevaMascota(Mascota mascota) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("nombre", mascota.getNombre());
        valoresParaInsertar.put("edad", mascota.getEdad());
        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public long registrarUsuario(User usuario) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("cedula", usuario.getCedula());
        valoresParaInsertar.put("nombre", usuario.getNombre());
        valoresParaInsertar.put("apellido", usuario.getApellido());
        valoresParaInsertar.put("telefono", usuario.getTelefono());
        valoresParaInsertar.put("correo", usuario.getCorreo());
        valoresParaInsertar.put("password", usuario.getPasswd());

        return baseDeDatos.insert(usuarios, null, valoresParaInsertar);
    }

    public long agendarCita(Citas cita) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("problema", cita.getProblema());
        valoresParaInsertar.put("marca", cita.getMarcaBicicleta());
        valoresParaInsertar.put("user", cita.getUsuario().getId());

        return baseDeDatos.insert(citas, null, valoresParaInsertar);
    }

    public int guardarCambios(Mascota mascotaEditada) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("nombre", mascotaEditada.getNombre());
        valoresParaActualizar.put("edad", mascotaEditada.getEdad());
        // where id...
        String campoParaActualizar = "id = ?";
        // ... = idMascota
        String[] argumentosParaActualizar = {String.valueOf(mascotaEditada.getId())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public List<Citas> getCitas() {
        List<Citas> citaList = new ArrayList<>();
        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();
        // SELECT nombre, edad, id
        String[] columnasAConsultar = {"problema", "marca","id", "user"};
        Cursor cursor = baseDeDatos.query(
                citas,//from mascotas
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return citaList;

        }
        // Si no hay datos, igualmente regresamos la lista vacía
        if (!cursor.moveToFirst()) return citaList;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de mascotas
        do {
            // El 0 es el número de la columna, como seleccionamos
            // nombre, edad,id entonces el nombre es 0, edad 1 e id es 2
            String problema = cursor.getString(0);
            String marca = cursor.getString(1);
            long idCita = cursor.getLong(2);
            int idUser = cursor.getInt(3);
            User userTmp = new User();
            userTmp.setId(idUser);
            Citas cita = new Citas(problema,marca,userTmp,idCita);
            citaList.add(cita);
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de mascotas :)
        cursor.close();
        return citaList;
    }

    public List<User> obtenerUsuarios() {
        List<User> usuariosList = new ArrayList<>();
        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();
        // SELECT nombre, edad, id
        String[] columnasAConsultar = {"password", "correo", "id"};
        Cursor cursor = baseDeDatos.query(
                usuarios,//from mascotas
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return usuariosList;

        }
        // Si no hay datos, igualmente regresamos la lista vacía
        if (!cursor.moveToFirst()) return usuariosList;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de mascotas
        do {
            // El 0 es el número de la columna, como seleccionamos
            // nombre, edad,id entonces el nombre es 0, edad 1 e id es 2
            String passwordBd = cursor.getString(0);
            String correoBd = cursor.getString(1);
            long idUser = cursor.getLong(2);
            User usuarioBd = new User(correoBd, passwordBd, idUser);
            usuariosList.add(usuarioBd);
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de mascotas :)
        cursor.close();
        return usuariosList;
    }

    public ArrayList<Mascota> obtenerMascotas() {
        ArrayList<Mascota> mascotas = new ArrayList<>();
        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();
        // SELECT nombre, edad, id
        String[] columnasAConsultar = {"nombre", "edad", "id"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,//from mascotas
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return mascotas;

        }
        // Si no hay datos, igualmente regresamos la lista vacía
        if (!cursor.moveToFirst()) return mascotas;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de mascotas
        do {
            // El 0 es el número de la columna, como seleccionamos
            // nombre, edad,id entonces el nombre es 0, edad 1 e id es 2
            String nombreObtenidoDeBD = cursor.getString(0);
            int edadObtenidaDeBD = cursor.getInt(1);
            long idMascota = cursor.getLong(2);
            Mascota mascotaObtenidaDeBD = new Mascota(nombreObtenidoDeBD, edadObtenidaDeBD, idMascota);
            mascotas.add(mascotaObtenidaDeBD);
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de mascotas :)
        cursor.close();
        return mascotas;
    }
}