-- Verificar y crear secuencia si no existe
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM user_sequences 
    WHERE sequence_name = 'PELICULA_SEQ';
    
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE SEQUENCE PELICULA_SEQ START WITH 1 INCREMENT BY 1';
    END IF;
END;
/

-- Insertar películas de ejemplo solo si no existen
MERGE INTO PELICULAS p
USING (
    SELECT 1 AS ID, 'Inception' AS TITULO, 2010 AS ANIO, 'Christopher Nolan' AS DIRECTOR, 'Ciencia Ficción' AS GENERO, 'Un ladrón de sueños recibe una tarea imposible.' AS SINOPSIS FROM DUAL
    UNION ALL
    SELECT 2, 'The Matrix', 1999, 'Hnos. Wachowski', 'Ciencia Ficción', 'Un hacker descubre la verdadera naturaleza de la realidad.' FROM DUAL
) n
ON (p.TITULO = n.TITULO)
WHEN NOT MATCHED THEN 
    INSERT (ID, TITULO, ANIO, DIRECTOR, GENERO, SINOPSIS)
    VALUES (PELICULA_SEQ.NEXTVAL, n.TITULO, n.ANIO, n.DIRECTOR, n.GENERO, n.SINOPSIS);