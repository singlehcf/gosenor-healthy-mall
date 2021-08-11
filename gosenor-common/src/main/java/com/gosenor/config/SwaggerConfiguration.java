package com.gosenor.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-21 09:55
 */
@Component
@EnableOpenApi
@Data
public class SwaggerConfiguration {
    @Bean
    public Docket webApiDocket(){
        return new
                Docket(DocumentationType.OAS_30)
                .groupName("⽤户端接⼝⽂档")
                .pathMapping("/")
        // 定义是否开启swagger，false为关闭，可以通过变ᰁ控制，线上关闭
                .enable(true)
                //配置api⽂档元信息
                .apiInfo(apiInfo())
                // 选择哪些接⼝作为swagger的doc发布
                .select()

                .apis(RequestHandlerSelectors.basePackage("com.gosenor"))
                //正则匹配请求路径，并分配⾄当前分组
                .paths(PathSelectors.ant("/api/**"))
                .paths(PathSelectors.any())
                .build()
                // 新版SwaggerUI3.0
                .globalRequestParameters(globalReqeustParameters())
                .globalResponses(HttpMethod.GET,getGlabalResponseMessage())
                .globalResponses(HttpMethod.POST,getGlabalResponseMessage());
    }

    @Bean
    public Docket adminApiDoc(){
        return new
                Docket(DocumentationType.OAS_30)
                .groupName("管理端接⼝⽂档")
                .pathMapping("/")
        // 定义是否开启swagger，false为关闭，可以通过变ᰁ控制，线上关闭
                .enable(true)
                //配置api⽂档元信息
                .apiInfo(apiInfo())
                // 选择哪些接⼝作为swagger的doc发布
                .select()

                .apis(RequestHandlerSelectors.basePackage("com.gosenor"))
                //正则匹配请求路径，并分配⾄当前分组
                .paths(PathSelectors.ant("/admin/**"))
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("大健康电商平台")
                .description("微服务接⼝⽂档")
                .contact(new Contact("国信利安科技有限公司", "http://www.gosenor.com/","794666918@qq.com")).version("12")
                .build();
    }

    /**
     * 配置全局通用参数
     *
     * @return
     */
    private List<RequestParameter> globalReqeustParameters() {

        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("登录令牌")
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());

//        parameters.add(new RequestParameterBuilder()
//                .name("token2")
//                .description("登录令牌")
//                .in(ParameterType.HEADER)
//                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
//                .required(false)
//                .build());

        return parameters;

    }


    /**
     * 生成通用的响应信息
     */

    private List<Response> getGlabalResponseMessage() {

        List<Response> list = new ArrayList<>();
        list.add(new ResponseBuilder()
                .code("4xx")
                .description("请求错误，根据code和msg检查")
                .build());

        return list;
    }


}
