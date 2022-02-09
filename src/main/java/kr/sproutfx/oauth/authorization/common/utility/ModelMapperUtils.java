package kr.sproutfx.oauth.authorization.common.utility;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperUtils {

    private ModelMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ModelMapper defaultMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
            .getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }   
}
