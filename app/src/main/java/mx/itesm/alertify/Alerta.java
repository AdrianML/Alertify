package mx.itesm.alertify;

public class Alerta {

    private String titulo;
    private double latitud;
    private double longitud;
    private String fecha;

    public Alerta(String titulo, double latitud, double longitud, String fecha){
        this.titulo = titulo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "[titulo: " + this.titulo + ", latitud: " + this.latitud + ", longitud: " + this.longitud + ", fecha: " + this.fecha + ", hora: " + "]";
    }
}
