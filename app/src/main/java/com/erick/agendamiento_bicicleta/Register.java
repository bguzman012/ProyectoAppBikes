package com.erick.agendamiento_bicicleta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erick.agendamiento_bicicleta.controllers.MascotasController;
import com.erick.agendamiento_bicicleta.modelos.User;

public class Register extends AppCompatActivity {

    private Button btnAgregarMascota, btnCancelarNuevaMascota;
    private EditText etCedula, etNombre, etApellido, etTelefono, etCorreo, etPasswd;
    private MascotasController mascotasController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Ojo: este código es generado automáticamente, pone la vista y ya, pero
        // no tiene nada que ver con el código que vamos a escribir
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etPasswd = findViewById(R.id.etPassw);

        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
        btnCancelarNuevaMascota = findViewById(R.id.btnCancelarNuevaMascota);

        // Lo siguiente sí es nuestro ;)
        // Definir nuestro controlador
        mascotasController = new MascotasController(Register.this);

        // Agregar listener del botón de guardar
        btnAgregarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resetear errores a ambos
                etCedula.setError(null);
                etNombre.setError(null);
                etApellido.setError(null);
                etTelefono.setError(null);
                etCorreo.setError(null);
                etPasswd.setError(null);
                String cedula = etCedula.getText().toString(),
                        nombre = etNombre.getText().toString(),
                        apellido = etApellido.getText().toString(),
                        telefono = etTelefono.getText().toString(),
                        correo = etCorreo.getText().toString(),
                        passwd = etPasswd.getText().toString();
                if ("".equals(nombre)) {
                    etNombre.setError("Escribe el nombre");
                    etNombre.requestFocus();
                    return;
                }
                if ("".equals(apellido)) {
                    etApellido.setError("Escribe el apellido");
                    etApellido.requestFocus();
                    return;
                }
                if ("".equals(cedula)) {
                    etCedula.setError("Escribe la cedula");
                    etCedula.requestFocus();
                    return;
                }
                if ("".equals(telefono)) {
                    etTelefono.setError("Escribe el telefono");
                    etTelefono.requestFocus();
                    return;
                }
                if ("".equals(correo)) {
                    etCorreo.setError("Escribe el correo");
                    etCorreo.requestFocus();
                    return;
                }
                if ("".equals(passwd)) {
                    etPasswd.setError("Escriba la contra");
                    etPasswd.requestFocus();
                    return;
                }

                User usuarioNuevo = new User(cedula, nombre, apellido, telefono, correo, passwd);
                // Ya pasó la validación

                long id = mascotasController.registrarUsuario(usuarioNuevo);
                if (id == -1) {
                    // De alguna manera ocurrió un error
                    Toast.makeText(Register.this, "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Registro guardado con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
}