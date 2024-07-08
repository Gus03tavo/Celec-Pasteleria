const express = require("express")
const mysql = require("mysql2")
const bodyparser=require("body-parser")
const multer = require('multer');

const app = express()
app.use(bodyparser.json())

const PUERTO=3000

app.use(bodyparser.json({ limit: '50mb' }));
app.use(bodyparser.urlencoded({ limit: '50mb', extended: true }));
//const upload = multer({ limits: { fileSize: 50 * 1024 * 1024 } });  // 50 MB

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });


// Middleware para manejar JSON y URL encoded
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const conexion=mysql.createConnection(
{
    host:'localhost',
    database:'servicesPasteleria',
    user:'root',
    password:'G2803@lon'
}


)
app.listen(PUERTO, ()=>{
    console.log("Servidor http://localhost:"+PUERTO)
})

conexion.connect(error => {
    if(error)throw error
    console.log("Conexión a BD exitosa")
})


app.get("/",(req,res)=>{
    res.send("Servicio corriendo")
})


// Obtener todos los usuarios
app.get("/usuarios", (req, res) => {
    const query = "SELECT * FROM usuarios";
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        res.json(resultado);
    });
});


// Obtener un usuario por su ID
app.get("/usuarios/:id", (req, res) => {
    const { id } = req.params;
    const query = "SELECT * FROM usuarios WHERE usu_id = ?";
    conexion.query(query, [id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.length === 0) {
            return res.status(404).send("Usuario no encontrado");
        }
        res.json(resultado[0]);
    });
});

//crear usuario
app.post("/usuarios", (req, res) => {
    const { usu_nombre, usu_apellidos, usu_email, usu_ncelular, usu_password, estado } = req.body;
    const query = "INSERT INTO usuarios (usu_nombre, usu_apellidos, usu_email, usu_ncelular, usu_password, estado) VALUES (?, ?, ?, ?, ?, ?)";
    conexion.query(query, [usu_nombre, usu_apellidos, usu_email, usu_ncelular, usu_password, estado], (error, resultado) => {
        if (error) {
            console.error("Error al insertar el usuario:", error);
            return res.status(500).send("Error del servidor");
        }
        res.json({ message: "Usuario creado correctamente", id: resultado.insertId });
    });
});


// Actualizar un usuario existente
app.put("/usuarios/:idBus", (req, res) => {
    const { idBus } = req.params;
    const { usu_nombre, usu_apellidos, usu_email, usu_ncelular } = req.body;
    const query = "UPDATE usuarios SET usu_nombre = ?, usu_apellidos = ?, usu_email = ?, usu_ncelular = ? WHERE usu_id = ?";
    conexion.query(query, [usu_nombre, usu_apellidos, usu_email, usu_ncelular, idBus], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Usuario no encontrado");
        }
        res.json({ message: "Usuario actualizado correctamente" });
    });
});


// Eliminar un usuario
app.delete("/usuarios/:id", (req, res) => {
    const { id } = req.params;
    const query = "DELETE FROM usuarios WHERE usu_id = ?";
    conexion.query(query, [id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Usuario no encontrado");
        }
        res.json({ message: "Usuario eliminado correctamente" });
    });
});





//cambiado
// Cambiar contraseña
app.put("/changePass", (req, res) => {
    
    const {usu_password,usu_email} = req.body;
    const query = "UPDATE usuarios SET usu_password=? where usu_email =?";
    conexion.query(query, [usu_password,usu_email], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Usuario no encontrado");
        }
        res.json({ message: "Contraseña actualizada correctamente" });
    });
});



//cambiado
// Listar todos los productos
app.get("/productos", (req, res) => {
    const query = "SELECT * FROM producto where estado!='0'";
    conexion.query(query, (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        const object={}
        object.productos=resultado
        res.json(object);
    });
});

//cambiado
// Buscar un producto por su ID
app.get("/productos/:id", (req, res) => {
    const { id } = req.params;
    const query = "SELECT * FROM producto WHERE pro_id = ?";
    conexion.query(query, [id], (error, resultado) => {
        const object={}
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.length === 0) {
            return res.status(404).send("Producto no encontrado");
        }
        object.producto=resultado[0]
        res.json(object);
    });
});


//canbiado
// Crear un nuevo producto
app.post("/productos", upload.single('pro_imagen'), (req, res) => {
    console.log("req.body:", req.body);
    console.log("req.file:", req.file);

    const { pro_nombre, pro_precio, pro_categoria, pro_stock, estado } = req.body;


    if (!req.file) {
        return res.status(400).send("Imagen no proporcionada");
    }

    const pro_imagen = req.file.buffer;
    const query = "INSERT INTO producto (pro_nombre, pro_precio, pro_categoria, pro_stock, pro_imagen, estado) VALUES (?, ?, ?, ?, ?, ?)";
    
    conexion.query(query, [pro_nombre, pro_precio, pro_categoria, pro_stock, pro_imagen, estado], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        res.json({ message: "Producto creado correctamente", id: resultado.insertId });
    });
});


//camviado
// Modificar un producto existente
app.put("/productos/:id",upload.single('pro_imagen'), (req, res) => {
    const { id } = req.params;
    console.log("req.body:", req.body);
    console.log("req.file:", req.file);

    const { pro_nombre, pro_precio, pro_categoria, pro_stock, estado } = req.body;
    if (!req.file) {
        return res.status(400).send("Imagen no proporcionada");
    }
    const pro_imagen = req.file.buffer;
   
    const query = "UPDATE producto SET pro_nombre = ?, pro_precio = ?, pro_categoria = ?, pro_stock = ?, pro_imagen = ?, estado = ? WHERE pro_id = ?";
    conexion.query(query, [pro_nombre, pro_precio, pro_categoria, pro_stock, pro_imagen, estado, id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Producto no encontrado");
        }
        res.json({ message: "Producto actualizado correctamente" });
    });
});


//cambiado
// Eliminar un producto
app.put("/productosDe/:id", (req, res) => {
    const { id } = req.params;
    const query = "UPDATE producto SET estado='0' where pro_id = ?";
    conexion.query(query, [id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Producto no encontrado");
        }
        res.json({ message: "Producto eliminado correctamente" });
    });
});

//cambiado
// Login cliente
app.post("/login/cliente", (req, res) => {
    const { usu_email, usu_password } = req.body;
    const query = `
        SELECT u.* FROM usuarios u
        JOIN user_roles ur ON u.usu_id = ur.user_id
        JOIN roles r ON ur.rol_id = r.rol_id
        WHERE u.usu_email = ? AND u.usu_password = ? AND r.name = 'CLIENTE'
    `;
    conexion.query(query, [usu_email, usu_password], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.length === 0) {
            return res.status(401).send("Credenciales inválidas");
        }
        res.json({ message: "Login exitoso", usuario: resultado[0] });
    });
});

// Login admin
app.post("/login/admin", (req, res) => {
    const { usu_email, usu_password } = req.body;
    const query = `
        SELECT u.* FROM usuarios u
        JOIN user_roles ur ON u.usu_id = ur.user_id
        JOIN roles r ON ur.rol_id = r.rol_id
        WHERE u.usu_email = ? AND u.usu_password = ? AND r.name = 'ADMIN'
    `;
    conexion.query(query, [usu_email, usu_password], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        if (resultado.length === 0) {
            return res.status(401).send("Credenciales inválidas");
        }
        res.json({ message: "Login exitoso", usuario: resultado[0] });
    });
});




// Agregar o actualizar un producto en el carrito

app.post("/carrito", (req, res) => {
    const { usu_id, pro_id, cantidad } = req.body;
    const querySelect = "SELECT * FROM Carrito WHERE usu_id = ? AND pro_id = ?";
    const queryInsert = "INSERT INTO Carrito (usu_id, pro_id, cantidad) VALUES (?, ?, ?)";
    const queryUpdate = "UPDATE Carrito SET cantidad = ? WHERE usu_id = ? AND pro_id = ?";
    
    conexion.query(querySelect, [usu_id, pro_id], (error, resultados) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        
        if (resultados.length > 0) {
            // El producto ya está en el carrito, actualizar la cantidad
            conexion.query(queryUpdate, [cantidad, usu_id, pro_id], (error, resultado) => {
                if (error) {
                    console.error(error.message);
                    return res.status(500).send("Error del servidor");
                }
                res.json({ message: "Cantidad actualizada en el carrito" });
            });
        } else {
            // El producto no está en el carrito, agregar nuevo
            conexion.query(queryInsert, [usu_id, pro_id, cantidad], (error, resultado) => {
                if (error) {
                    console.error(error.message);
                    return res.status(500).send("Error del servidor");
                }
                res.json({ message: "Producto agregado al carrito", id: resultado.insertId });
            });
        }
    });
});




app.get("/carrito/:usu_id", (req, res) => {
    const { usu_id } = req.params;
    const query = `
        SELECT 
            p.pro_id,
            p.pro_nombre,
            p.pro_precio,
            p.pro_imagen,
            c.cantidad
        FROM 
            Carrito c
        JOIN 
            producto p ON c.pro_id = p.pro_id
        WHERE 
            c.usu_id = ?;
    `;
    conexion.query(query, [usu_id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        const object={}
        object.productos=resultado
        res.json(object);
    });
});





// Calcular el total, IGV y subtotal de la orden del cliente
app.get("/carritoTotal/:usu_id", (req, res) => {
    const { usu_id } = req.params;
    const query = `
        SELECT 
            SUM(p.pro_precio * c.cantidad) AS subtotal,
            SUM(p.pro_precio * c.cantidad) * 0.18 AS igv,
            SUM(p.pro_precio * c.cantidad)  AS total
        FROM 
            Carrito c
        JOIN 
            producto p ON c.pro_id = p.pro_id
        WHERE 
            c.usu_id = ?;
    `;
    conexion.query(query, [usu_id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        res.json(resultado[0]);
    });
});








app.delete("/carrito/:usu_id/:pro_nombre", (req, res) => {
    const { usu_id, pro_nombre } = req.params;
    console.log(`Eliminando producto ${pro_nombre} del carrito del usuario ${usu_id}`);
    
    // Consulta para obtener el ID del producto basado en el nombre
    const query = "SELECT pro_id FROM producto WHERE pro_nombre = ?";
    conexion.query(query, [pro_nombre], (error, resultados) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        
        if (resultados.length === 0) {
            return res.status(404).send("Producto no encontrado");
        }
        
        const pro_id = resultados[0].pro_id;
        
        // Ahora realizas la eliminación del carrito usando el pro_id obtenido
        const deleteQuery = "DELETE FROM carrito WHERE usu_id = ? AND pro_id = ?";
        conexion.query(deleteQuery, [usu_id, pro_id], (err, resultado) => {
            if (err) {
                console.error(err.message);
                return res.status(500).send("Error del servidor");
            }
            res.json({ message: "Producto eliminado correctamente del carrito" });
        });
    });
});



// Eliminar todos los productos del carrito de un usuario
app.delete("/carrito/:usu_id", (req, res) => {
    const { usu_id } = req.params;
    const query = "DELETE FROM Carrito WHERE usu_id = ?";
    conexion.query(query, [usu_id], (error, resultado) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error del servidor");
        }
        res.json({ message: "Carrito vaciado correctamente" });
    });
});





//REALIZAR VENTA, DETALLEVENTA
app.post("/registrarVenta", (req, res) => {
    const { usu_id, total } = req.body;
    const queryVenta = "INSERT INTO venta (v_fecha_venta, v_total_venta, usu_id) VALUES (NOW(), ?, ?)";
    const queryDetalleVenta = "INSERT INTO detalle_venta (deve_cant_vendida, deve_precio_producto, deve_sub_total, pro_id, v_id) VALUES (?, ?, ?, ?, ?)";
    const queryCarrito = "SELECT c.pro_id, c.cantidad, p.pro_precio FROM Carrito c INNER JOIN producto p ON c.pro_id = p.pro_id WHERE c.usu_id = ?";
    const queryVaciarCarrito = "DELETE FROM Carrito WHERE usu_id = ?";
    const queryUpdateStock = "UPDATE producto SET pro_stock = pro_stock - ? WHERE pro_id = ?";

    conexion.query(queryVenta, [total, usu_id], (error, resultadoVenta) => {
        if (error) {
            console.error(error.message);
            return res.status(500).send("Error al registrar la venta");
        }

        const v_id = resultadoVenta.insertId;

        conexion.query(queryCarrito, [usu_id], (error, productosCarrito) => {
            if (error) {
                console.error(error.message);
                return res.status(500).send("Error al obtener productos del carrito");
            }

            productosCarrito.forEach((producto) => {
                const { cantidad, pro_id, pro_precio } = producto;
                const sub_total = cantidad * pro_precio;
                conexion.query(queryDetalleVenta, [cantidad, pro_precio, sub_total, pro_id, v_id], (error) => {
                    if (error) {
                        console.error(error.message);
                        return res.status(500).send("Error al insertar detalles de la venta");
                    }

                    // Actualizar el stock del producto
                    conexion.query(queryUpdateStock, [cantidad, pro_id], (error) => {
                        if (error) {
                            console.error(error.message);
                            return res.status(500).send("Error al actualizar el stock del producto");
                        }
                    });
                });
            });

            conexion.query(queryVaciarCarrito, [usu_id], (error) => {
                if (error) {
                    console.error(error.message);
                    return res.status(500).send("Error al vaciar el carrito");
                }

                // Envía la respuesta solo después de completar todas las operaciones
                res.json({ message: "Venta registrada, carrito vaciado y stock actualizado correctamente" });
            });
        });
    });
});


app.get('/historial-ventas/:usuarioId', (req, res) => {
    const usuarioId = req.params.usuarioId;

    // Consulta SQL para obtener el historial de ventas por usuario
    const sql = "SELECT p.pro_imagen, p.pro_nombre, p.pro_precio, d.deve_cant_vendida, p.pro_precio * d.deve_cant_vendida AS total_venta, v.v_id FROM producto p INNER JOIN detalle_venta d ON p.pro_id = d.pro_id INNER JOIN venta v ON v.v_id = d.v_id WHERE v.usu_id = ? ORDER BY v.v_id;"

    // Ejecutar la consulta SQL
    conexion.query(sql, [usuarioId], (err, results) => {
        if (err) {
            console.error('Error al ejecutar la consulta SQL:', err);
            res.status(500).json({ error: 'Error interno del servidor' });
            return;
        }

        // Construir el objeto de respuesta esperado por la aplicación Android
        const historialProductos = results.map(row => ({
            pro_imagen: row.pro_imagen,
            pro_nombre: row.pro_nombre,
            pro_precio: row.pro_precio,
            deve_cant_vendida: row.deve_cant_vendida,
            total_venta: row.total_venta,
            v_id: row.v_id
        }));

        // Devolver los resultados como un objeto JSON con la estructura esperada
        res.json({ historialprod: historialProductos });
    });
});


