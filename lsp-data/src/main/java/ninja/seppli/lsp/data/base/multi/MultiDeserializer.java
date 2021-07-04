package ninja.seppli.lsp.data.base.multi;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

/**
 * Deserializes multi object in jackson
 *
 * See at <a href=
 * "https://newbedev.com/how-to-create-a-custom-deserializer-in-jackson-for-a-generic-type">https://newbedev.com/how-to-create-a-custom-deserializer-in-jackson-for-a-generic-type</>
 * for more information
 *
 * @author sebi
 *
 */
public class MultiDeserializer extends JsonDeserializer<Multi<?, ?>> implements ContextualDeserializer {
	/**
	 * type 1
	 */
	private JavaType t1;
	/**
	 * type 2
	 */
	private JavaType t2;

	/**
	 * Constructor
	 */
	public MultiDeserializer() {
	}

	/**
	 * Internal Constructor
	 *
	 * @param t1 t1 type
	 * @param t2 t2 type
	 */
	private MultiDeserializer(JavaType t1, JavaType t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
			throws JsonMappingException {
		JavaType wrapperType = property != null ? property.getType() : ctxt.getContextualType();
		if (wrapperType.containedTypeCount() == 2) {
			JavaType t1 = wrapperType.containedType(0);
			JavaType t2 = wrapperType.containedType(1);
			return new MultiDeserializer(t1, t2);
		} else {
			// not enough type info is avaible --> return null
			return null;
		}
	}

	@Override
	public Multi<?, ?> deserialize(JsonParser p, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		try {
			// trying to parse with type t1
			return Multi.ofT1(ctx.readValue(p, t1));
		} catch (IOException e1) {
			try {
				// if this fails trying with type t2
				return Multi.ofT2(ctx.readValue(p, t2));
			} catch (IOException e2) {
				// if this fails to, throw multi exception
				throw new MultiException("Couldn't parse either Type " + t1 + " or " + t2, e1, e1);
			}
		}
	}
}
