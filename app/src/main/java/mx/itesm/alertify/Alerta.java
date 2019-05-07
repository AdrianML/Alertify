package mx.itesm.alertify;

public class Alerta {

    private String titulo;
    private double latitud;
    private double longitud;

    public Alerta(String titulo, double latitud, double longitud){
        this.titulo = titulo;
        this.latitud = latitud;
        this.longitud = longitud;
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

    @Override
    public String toString() {
        return "[titulo: " + this.titulo + ", latitud: " + this.latitud + ", longitud: " + this.longitud + "]";
    }
}
