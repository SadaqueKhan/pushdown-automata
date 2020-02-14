package app.shared;

import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class MarshallerListener extends Marshaller.Listener {

    private XMLStreamWriter xsw;

    public MarshallerListener(XMLStreamWriter xsw) {
        this.xsw = xsw;
    }

    @Override
    public void beforeMarshal(Object source) {
        try {
            xsw.writeComment("Before:  " + source.toString());
        } catch (XMLStreamException e) {
            // TODO: handle exception
        }
    }

    @Override
    public void afterMarshal(Object source) {
        try {
            xsw.writeComment("After:  " + source.toString());
        } catch (XMLStreamException e) {
            // TODO: handle exception
        }
    }

}