package panda.mvc.fileupload;

import java.io.IOException;
import java.lang.reflect.Type;

import panda.castor.CastContext;
import panda.castor.CastException;
import panda.castor.Castor;
import panda.lang.codec.binary.Base64;



public class UploadFileCastor<S, T extends UploadFile> extends Castor<Object, UploadFile> {
	public UploadFileCastor(Type toType) {
		super(Object.class, toType);
	}
	
	@Override
	protected UploadFile castValue(Object value, CastContext context) {
		if (value instanceof String) {
			value = Base64.decodeBase64((String)value);
		}
		
		if (value instanceof byte[]) {
			UploadFile uf = createTarget();
			try {
				uf.setData((byte[])value);
			}
			catch (IOException e) {
				throw new CastException("Failed to convert UploadFile: " + value);
			}
			return uf;
		}

		throw new CastException("Failed to convert UploadFile: " + value.getClass());
	}
}
