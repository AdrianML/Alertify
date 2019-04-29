package mx.itesm.alertify;

public class Report {
    private int idReporte;
    private String titulo;
    private String fecha;
    private String hora;
    private String desc;
    private double latitud;
    private double longitud;

    public Report(int idReporte, String titulo, String fecha, String hora, String desc, double latitud, double longitud){
        this.idReporte = idReporte;
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
        this.desc = desc;
        this.latitud = latitud;
        this.longitud = longitud;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }


}
