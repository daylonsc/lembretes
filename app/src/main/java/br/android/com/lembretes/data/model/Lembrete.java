package br.android.com.lembretes.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.Date;

public class Lembrete implements Parcelable {
    private int id;
    private String lembrete;
    private Date dataHora;

    public Lembrete() {

    }

    public static final class  LembreteEntry implements BaseColumns {
        public static final String TABLE_NAME = "lembrete";

        public static final String COLUNA_DESCRICAO_LEMBRETE = "descricao";
        public static final String COLUNA_DATA_HORA = "data_hora";
    }

    public Lembrete(Parcel in) {
        id = in.readInt();
        lembrete = in.readString();
    }

    public static final Creator<Lembrete> CREATOR = new Creator<Lembrete>() {
        @Override
        public Lembrete createFromParcel(Parcel in) {
            return new Lembrete(in);
        }

        @Override
        public Lembrete[] newArray(int size) {
            return new Lembrete[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLembrete() {
        return lembrete;
    }

    public void setLembrete(String lembrete) {
        this.lembrete = lembrete;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(lembrete);
    }
}
