package it.intesys.codylab.rookie.exceptions;

import it.intesys.codylab.rookie.domain.Doctor;

public class NotFound extends RuntimeException {
    private Class<?> type;
    private  Object id;
    public NotFound(Class<?> type, Object id) {
        this.type = type;
        this.id = id;
    }

    public Class<?> getType() {
        return type;
    }
    public Object getId(){
        return id;
    }
    @Override
    public  String getMessage(){
        return  toString();
    }
    @Override
    public String toString(){
        return  String.format("Not found %s [%s]", type.getSimpleName(), id);
    }
}
