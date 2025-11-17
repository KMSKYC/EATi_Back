package msyc.eati.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringListConverter : AttributeConverter<List<String>?, String?> {
    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): List<String>? {
        return dbData?.let {
            objectMapper.readValue(it, object : TypeReference<List<String>>() {})
        }
    }
}
