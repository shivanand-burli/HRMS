// package com.hrms.app.config;

// import java.util.Objects;

// import org.eclipse.microprofile.openapi.OASFactory;
// import org.eclipse.microprofile.openapi.OASFilter;
// import org.eclipse.microprofile.openapi.models.OpenAPI;
// import org.eclipse.microprofile.openapi.models.parameters.Parameter;

// import com.hrms.app.util.Constants;

// @io.quarkus.smallrye.openapi.OpenApiFilter(io.quarkus.smallrye.openapi.OpenApiFilter.RunStage.BUILD)
// public class OpenApiFilter implements OASFilter {

//     @Override
//     public void filterOpenAPI(OpenAPI openApi) {
//         // Add a global header parameter to all operations
//         openApi.getPaths().getPathItems().values().stream().filter(Objects::nonNull).forEach(pathItem -> {
//             pathItem.getOperations().values().stream().filter(Objects::nonNull).forEach(operation -> {
//                 final Parameter companyHeader = OASFactory.createParameter()
//                         .name(Constants.X_CMP_ID)
//                         .in(Parameter.In.HEADER)
//                         .description("Specify Company ID")
//                         .required(false)
//                         .schema(OASFactory.createSchema());

//                 final Parameter employeeHeader = OASFactory.createParameter()
//                         .name(Constants.X_EMP_ID)
//                         .in(Parameter.In.HEADER)
//                         .description("Specify Employee ID")
//                         .required(false)
//                         .schema(OASFactory.createSchema());

//                 operation.addParameter(companyHeader);
//                 operation.addParameter(employeeHeader);
//             });
//         });
//     }
// }