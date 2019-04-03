package mx.itesm.alertify;

public class Report {
    private String titulo;
    private String fecha;
    private String hora;
    private String desc;

    public Report(String titulo, String fecha, String hora, String desc){
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
        this.desc = desc;
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
}
