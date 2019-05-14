package mx.itesm.alertify;

public class Alerta {

    private String titulo;
    private double latitud;
    private double longitud;
    private String fecha;
    private String hora;

    public Alerta(String titulo, double latitud, double longitud, String fecha, String hora){
        this.titulo = titulo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "[titulo: " + this.titulo + ", latitud: " + this.latitud + ", longitud: " + this.longitud + ", fecha: " + this.fecha + ", hora: " + this.hora + "]";
    }
}
