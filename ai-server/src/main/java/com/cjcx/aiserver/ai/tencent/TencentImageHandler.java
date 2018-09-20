package com.cjcx.aiserver.ai.tencent;

import com.cjcx.aiserver.ai.AbstractImageHandler;
import com.cjcx.aiserver.ai.config.AbstractConfig;
import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.ai.tencent.entity.Item;
import com.cjcx.aiserver.ai.tencent.entity.ItemsData;
import com.cjcx.aiserver.ai.tencent.entity.WordsResponse;
import com.cjcx.aiserver.ai.utils.HttptUtils;
import com.cjcx.aiserver.obj.ResultObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * API地址 腾讯云
 * https://cloud.tencent.com/document/product/641/12428#.E8.AE.A1.E8.B4.B9.E8.AF.B4.E6.98.8E
 * <p>
 * <p>
 * 计费说明
 * https://cloud.tencent.com/document/product/866/17619
 */
@Slf4j
@Component
public class TencentImageHandler extends AbstractImageHandler {

    /**
     * 默认 高精度&位置
     *
     * @return
     */
    @Override
    public ResultObject execute(AbstractConfig config) {
        ResultObject r = new ResultObject();

        imageConfig = (ImageConfig)config;

        String secretId = null;
        try {
            if (imageConfig.getLevel() != 2) {
                r.setErrCode("-1");
                r.setMsg("请求精度错误,请用高精度URL发起请求");
                return r;
            }

            TencentAccountManager.Account account = TencentAccountManager.getImageDefaultAccount();
            secretId = account.getAppId() + "";
            //签名
            String sign = Sign.appSign(account.getAppId(), account.getSecretId(), account.getSecretKey(), TencentConstant.BUCKETNAME_IMAGE, 10);

            //构造参数
            JSONObject json = new JSONObject();
            json.put("appid", account.getAppId() + "");
            json.put("bucket", TencentConstant.BUCKETNAME_IMAGE);
            json.put("url", imageConfig.getUrl());

            Map<String, String> map = new HashMap<String, String>();
            map.put("Host", "recognition.image.myqcloud.com");
            map.put("Content-Type", "application/json;charset=utf-8");
            map.put("Authorization", sign);

            String content = HttptUtils.doPost(TencentConstant.url_general, json.toString(), map);

            log.info("Tencent 结果:" + content);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //本地缓存更新
            limit(secretId, TencentConstant.url_general);
        }
        return r;
    }


    @Override
    protected void afterExecute(String content, ResultObject r) {
        Gson gson = new Gson();
        WordsResponse wr = gson.fromJson(content, WordsResponse.class);

        if (wr.getCode() != 0) {
            r.setErrCode(wr.getCode() + "");
            r.setMsg(wr.getMessage());
            return;
        }

        ItemsData itemsData = wr.getData();
        List<Item> list = itemsData.getItems();

        StringBuffer result = new StringBuffer();

        Map<Integer, String> map = new HashMap<>();
        List<Integer> tops = new ArrayList<>();
        Item item = null;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            tops.add(item.getItemcoord().getY());
            map.put(item.getItemcoord().getY(), item.getItemstring());
        }

        Collections.sort(tops);          //排序(默认升序)
        /*System.out.println("===================处理前======================");
        final StringBuffer tm = new StringBuffer();
        tops.stream().map((e) -> map.get(e)).forEach((e) -> tm.append(e).append("\r\n"));
        System.out.println(tm);*/

        Integer pre = 0;
        for (int i = 0; i < tops.size(); i++) {
            Integer now = tops.get(i);
            if (i >= 1) {
                if (now <= pre + FAULT_TOLERANT_VALUE) {
                    //在容错范围内, 即在同一行
                    String nowLine = map.get(now);           //当前行的值
                    map.put(pre, map.get(pre) + nowLine);
                    map.remove(now);
                }
            }
            pre = now;
        }

        tops.clear();
        for (Map.Entry<Integer, String> o : map.entrySet()) {
            tops.add(o.getKey());
        }
        Collections.sort(tops);
        for (int i = 0; i < tops.size(); i++) {
            result.append(map.get(tops.get(i))).append("\r\n");
        }
        /*System.out.println("===================处理后======================");
        System.out.println(result.toString());*/

        r.setData(result.toString());
    }


    public static void main(String[] args) {
        ImageConfig config = new ImageConfig();
        config.setLevel(2);

        TencentImageHandler ha = new TencentImageHandler();
        ha.setImageConfig(config);

        String content = "{\"code\":0,\"message\":\"OK\",\"data\":{\"class\":[],\"angle\":-1.375,\"items\":[{\"itemcoord\":{\"x\":118,\"y\":159,\"width\":629,\"height\":99},\"words\":[{\"character\":\"P\",\"confidence\":0.9448614120483398},{\"character\":\"u\",\"confidence\":0.9998899698257446},{\"character\":\"r\",\"confidence\":0.999595820903778},{\"character\":\"C\",\"confidence\":0.9994068145751952},{\"character\":\"o\",\"confidence\":0.9976279139518738},{\"character\":\"l\",\"confidence\":0.6492615342140198},{\"character\":\"l\",\"confidence\":0.9231365323066713},{\"character\":\"o\",\"confidence\":0.9981278777122498},{\"character\":\"n\",\"confidence\":0.9986823201179504}],\"itemstring\":\"PurCollon\"},{\"itemcoord\":{\"x\":203,\"y\":258,\"width\":294,\"height\":53},\"words\":[{\"character\":\"州\",\"confidence\":0.9993045330047609},{\"character\":\"天\",\"confidence\":0.998504877090454},{\"character\":\"汇\",\"confidence\":0.9994195699691773},{\"character\":\"广\",\"confidence\":0.9968461394309998},{\"character\":\"场\",\"confidence\":0.9999548196792604},{\"character\":\"店\",\"confidence\":0.9998493194580078}],\"itemstring\":\"州天汇广场店\"},{\"itemcoord\":{\"x\":158,\"y\":312,\"width\":618,\"height\":49},\"words\":[{\"character\":\"网\",\"confidence\":0.9999889135360718},{\"character\":\"上\",\"confidence\":0.9999343156814576},{\"character\":\"商\",\"confidence\":0.9999806880950928},{\"character\":\"城\",\"confidence\":0.9994476437568665},{\"character\":\":\",\"confidence\":0.8007650375366211},{\"character\":\"w\",\"confidence\":0.9973217844963074},{\"character\":\"w\",\"confidence\":0.9995577931404114},{\"character\":\"w\",\"confidence\":0.998388171195984},{\"character\":\".\",\"confidence\":0.9873726963996888},{\"character\":\"p\",\"confidence\":0.6442921757698059},{\"character\":\"u\",\"confidence\":0.999857783317566},{\"character\":\"r\",\"confidence\":0.9944056272506714},{\"character\":\"c\",\"confidence\":0.9905827045440674},{\"character\":\"o\",\"confidence\":0.9992871880531312},{\"character\":\"t\",\"confidence\":0.9983559250831604},{\"character\":\"t\",\"confidence\":0.9969557523727416},{\"character\":\"o\",\"confidence\":0.9969584941864014},{\"character\":\"n\",\"confidence\":0.9757702350616456},{\"character\":\".\",\"confidence\":0.9971160888671876},{\"character\":\"c\",\"confidence\":0.9948427081108092},{\"character\":\"o\",\"confidence\":0.9988613128662108},{\"character\":\"m\",\"confidence\":0.9992666840553284}],\"itemstring\":\"网上商城:www.purcotton.com\"},{\"itemcoord\":{\"x\":155,\"y\":364,\"width\":476,\"height\":49},\"words\":[{\"character\":\"收\",\"confidence\":0.9999756813049316},{\"character\":\"银\",\"confidence\":0.9999785423278808},{\"character\":\"台\",\"confidence\":0.9930115342140198},{\"character\":\":\",\"confidence\":0.9975050091743468},{\"character\":\"1\",\"confidence\":0.9713318347930908},{\"character\":\"工\",\"confidence\":0.9967949986457824},{\"character\":\"号\",\"confidence\":0.9999979734420776},{\"character\":\":\",\"confidence\":0.9991948008537293},{\"character\":\"1\",\"confidence\":0.9976446032524108},{\"character\":\"2\",\"confidence\":0.999896764755249},{\"character\":\"1\",\"confidence\":0.9997499585151672},{\"character\":\"5\",\"confidence\":0.9997029900550842},{\"character\":\"9\",\"confidence\":0.9997342228889464},{\"character\":\"8\",\"confidence\":0.998664140701294}],\"itemstring\":\"收银台:1工号:121598\"},{\"itemcoord\":{\"x\":157,\"y\":569,\"width\":519,\"height\":49},\"words\":[{\"character\":\"1\",\"confidence\":0.9939152598381044},{\"character\":\"0\",\"confidence\":0.9902993440628052},{\"character\":\"0\",\"confidence\":0.98565673828125},{\"character\":\"1\",\"confidence\":0.9989577531814576},{\"character\":\"7\",\"confidence\":0.9992578625679016},{\"character\":\"2\",\"confidence\":0.999330997467041},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"X\",\"confidence\":0.9624784588813782},{\"character\":\"P\",\"confidence\":0.996118664741516},{\"character\":\"D\",\"confidence\":0.9661287665367128},{\"character\":\"0\",\"confidence\":0.5457069873809815},{\"character\":\"4\",\"confidence\":0.9899882674217224},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"6\",\"confidence\":0.8404250144958496},{\"character\":\"8\",\"confidence\":0.9808586239814758},{\"character\":\"9\",\"confidence\":0.9879236817359924},{\"character\":\"6\",\"confidence\":0.7838307619094849}],\"itemstring\":\"100172 XPD04 6896\"},{\"itemcoord\":{\"x\":163,\"y\":622,\"width\":337,\"height\":43},\"words\":[{\"character\":\"日\",\"confidence\":0.9824276566505432},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"期\",\"confidence\":0.9999994039535524},{\"character\":\":\",\"confidence\":0.9970017075538636},{\"character\":\"2\",\"confidence\":0.9991503953933716},{\"character\":\"0\",\"confidence\":0.9997963309288024},{\"character\":\"1\",\"confidence\":0.999228835105896},{\"character\":\"8\",\"confidence\":0.9990015625953674},{\"character\":\"-\",\"confidence\":0.995218813419342},{\"character\":\"0\",\"confidence\":0.9997472167015076},{\"character\":\"5\",\"confidence\":0.9987034797668456},{\"character\":\"-\",\"confidence\":0.981036901473999},{\"character\":\"3\",\"confidence\":0.9989641904830932},{\"character\":\"0\",\"confidence\":0.9988561868667604}],\"itemstring\":\"日 期:2018-05-30\"},{\"itemcoord\":{\"x\":177,\"y\":672,\"width\":198,\"height\":48},\"words\":[{\"character\":\"货\",\"confidence\":0.9998227953910828},{\"character\":\"品\",\"confidence\":0.9997965693473816},{\"character\":\"编\",\"confidence\":0.9998507499694824},{\"character\":\"号\",\"confidence\":0.9980069994926452}],\"itemstring\":\"货品编号\"},{\"itemcoord\":{\"x\":540,\"y\":667,\"width\":400,\"height\":55},\"words\":[{\"character\":\"零\",\"confidence\":0.5963007807731628},{\"character\":\"售\",\"confidence\":0.9999454021453856},{\"character\":\"价\",\"confidence\":0.997870683670044},{\"character\":\"~\",\"confidence\":0.4696474671363831},{\"character\":\"数\",\"confidence\":0.9983232617378236},{\"character\":\"量\",\"confidence\":0.9999408721923828},{\"character\":\"总\",\"confidence\":0.999729573726654},{\"character\":\"额\",\"confidence\":0.955594837665558}],\"itemstring\":\"零售价~数量总额\"},{\"itemcoord\":{\"x\":178,\"y\":717,\"width\":495,\"height\":43},\"words\":[{\"character\":\"2\",\"confidence\":0.9988020658493042},{\"character\":\"3\",\"confidence\":0.9948657155036926},{\"character\":\"0\",\"confidence\":0.99978107213974},{\"character\":\"0\",\"confidence\":0.9999229907989502},{\"character\":\"0\",\"confidence\":0.9995276927947998},{\"character\":\"1\",\"confidence\":0.9947015047073364},{\"character\":\"3\",\"confidence\":0.9973627924919128},{\"character\":\"5\",\"confidence\":0.9974465370178224},{\"character\":\"1\",\"confidence\":0.9953649044036864},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"1\",\"confidence\":0.9993649125099182},{\"character\":\"-\",\"confidence\":0.9906424880027772},{\"character\":\"0\",\"confidence\":0.9985211491584778},{\"character\":\"0\",\"confidence\":0.6417502760887146},{\"character\":\"0\",\"confidence\":0.9917709827423096},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"1\",\"confidence\":0.9987976551055908},{\"character\":\"6\",\"confidence\":0.9992671608924866},{\"character\":\"8\",\"confidence\":0.9981778860092164},{\"character\":\".\",\"confidence\":0.9653877019882202},{\"character\":\"0\",\"confidence\":0.997102916240692},{\"character\":\"0\",\"confidence\":0.9895334839820862}],\"itemstring\":\"230001351 1-000 168.00\"},{\"itemcoord\":{\"x\":718,\"y\":722,\"width\":138,\"height\":37},\"words\":[{\"character\":\"1\",\"confidence\":0.9957363605499268},{\"character\":\"1\",\"confidence\":0.9998131394386292},{\"character\":\"5\",\"confidence\":0.9991627931594848},{\"character\":\"4\",\"confidence\":0.9995346069335938},{\"character\":\".\",\"confidence\":0.9897745847702026},{\"character\":\"5\",\"confidence\":0.9966089725494384},{\"character\":\"0\",\"confidence\":0.9977237582206726}],\"itemstring\":\"1154.50\"},{\"itemcoord\":{\"x\":179,\"y\":773,\"width\":729,\"height\":49},\"words\":[{\"character\":\"线\",\"confidence\":0.998671531677246},{\"character\":\"条\",\"confidence\":0.99985933303833},{\"character\":\"帆\",\"confidence\":0.9998719692230224},{\"character\":\"船\",\"confidence\":0.9996808767318726},{\"character\":\"幼\",\"confidence\":0.9996125102043152},{\"character\":\"儿\",\"confidence\":0.9995417594909668},{\"character\":\"双\",\"confidence\":0.99969744682312},{\"character\":\"层\",\"confidence\":0.9999607801437378},{\"character\":\"纱\",\"confidence\":0.9997827410697936},{\"character\":\"布\",\"confidence\":0.9998181462287904},{\"character\":\"印\",\"confidence\":0.9929038882255554},{\"character\":\"花\",\"confidence\":0.9999794960021972},{\"character\":\"空\",\"confidence\":0.9999748468399048},{\"character\":\"调\",\"confidence\":0.9998778104782105},{\"character\":\"被\",\"confidence\":0.999449908733368}],\"itemstring\":\"线条帆船幼儿双层纱布印花空调被\"},{\"itemcoord\":{\"x\":183,\"y\":826,\"width\":239,\"height\":41},\"words\":[{\"character\":\"1\",\"confidence\":0.989968717098236},{\"character\":\"3\",\"confidence\":0.999653458595276},{\"character\":\"5\",\"confidence\":0.9993764758110046},{\"character\":\"x\",\"confidence\":0.9975001215934752},{\"character\":\"1\",\"confidence\":0.9704571962356569},{\"character\":\"2\",\"confidence\":0.9989626407623292},{\"character\":\"0\",\"confidence\":0.9930487871170044},{\"character\":\"c\",\"confidence\":0.98590487241745},{\"character\":\"m\",\"confidence\":0.9994702935218812},{\"character\":\",\",\"confidence\":0.9191980957984924}],\"itemstring\":\"135x120cm,\"},{\"itemcoord\":{\"x\":478,\"y\":823,\"width\":173,\"height\":70},\"words\":[{\"character\":\"1\",\"confidence\":0.8888536691665649},{\"character\":\"条\",\"confidence\":0.9972834587097168},{\"character\":\"装\",\"confidence\":0.9919341802597046},{\"character\":\"四\",\"confidence\":0.4375932216644287}],\"itemstring\":\"1条装四\"},{\"itemcoord\":{\"x\":162,\"y\":872,\"width\":77,\"height\":36},\"words\":[{\"character\":\"0\",\"confidence\":0.9743950366973876},{\"character\":\".\",\"confidence\":0.9614155292510986},{\"character\":\"0\",\"confidence\":0.995457887649536},{\"character\":\"0\",\"confidence\":0.9927743673324584}],\"itemstring\":\"0.00\"},{\"itemcoord\":{\"x\":173,\"y\":971,\"width\":121,\"height\":46},\"words\":[{\"character\":\"总\",\"confidence\":0.999953269958496},{\"character\":\"数\",\"confidence\":0.999958038330078},{\"character\":\":\",\"confidence\":0.9980720281600952},{\"character\":\"1\",\"confidence\":0.9862558245658876}],\"itemstring\":\"总数:1\"},{\"itemcoord\":{\"x\":173,\"y\":1021,\"width\":175,\"height\":45},\"words\":[{\"character\":\"总\",\"confidence\":0.999948024749756},{\"character\":\"额\",\"confidence\":0.9998905658721924},{\"character\":\":\",\"confidence\":0.9969653487205504},{\"character\":\"1\",\"confidence\":0.999721109867096},{\"character\":\"6\",\"confidence\":0.999565064907074},{\"character\":\"8\",\"confidence\":0.999488115310669}],\"itemstring\":\"总额:168\"},{\"itemcoord\":{\"x\":172,\"y\":1070,\"width\":205,\"height\":47},\"words\":[{\"character\":\"折\",\"confidence\":0.9993454813957214},{\"character\":\"扣\",\"confidence\":0.7447148561477661},{\"character\":\":\",\"confidence\":0.9861852526664734},{\"character\":\"-\",\"confidence\":0.990045130252838},{\"character\":\"1\",\"confidence\":0.9992333650588988},{\"character\":\"3\",\"confidence\":0.999666690826416},{\"character\":\".\",\"confidence\":0.9971243739128112},{\"character\":\"5\",\"confidence\":0.9964356422424316}],\"itemstring\":\"折扣:-13.5\"},{\"itemcoord\":{\"x\":174,\"y\":1119,\"width\":210,\"height\":43},\"words\":[{\"character\":\"净\",\"confidence\":0.9997875094413756},{\"character\":\"价\",\"confidence\":0.9996121525764464},{\"character\":\":\",\"confidence\":0.996283233165741},{\"character\":\"1\",\"confidence\":0.9961293935775756},{\"character\":\"5\",\"confidence\":0.9997318387031556},{\"character\":\"4\",\"confidence\":0.9935198426246644},{\"character\":\".\",\"confidence\":0.923369526863098},{\"character\":\"5\",\"confidence\":0.9984031319618224}],\"itemstring\":\"净价:154.5\"},{\"itemcoord\":{\"x\":177,\"y\":1166,\"width\":233,\"height\":46},\"words\":[{\"character\":\"已\",\"confidence\":0.6575427055358887},{\"character\":\"付\",\"confidence\":0.9998533725738524},{\"character\":\":\",\"confidence\":0.9980937838554382},{\"character\":\"1\",\"confidence\":0.9979445338249208},{\"character\":\"5\",\"confidence\":0.9995397329330444},{\"character\":\"4\",\"confidence\":0.999397873878479},{\"character\":\"\",\"confidence\":1.0},{\"character\":\"5\",\"confidence\":0.994843363761902},{\"character\":\"0\",\"confidence\":0.9954714775085448}],\"itemstring\":\"已付:154 50\"},{\"itemcoord\":{\"x\":175,\"y\":1214,\"width\":187,\"height\":47},\"words\":[{\"character\":\"找\",\"confidence\":0.9945882558822632},{\"character\":\"赎\",\"confidence\":0.9998154044151306},{\"character\":\":\",\"confidence\":0.9858031272888184},{\"character\":\"0\",\"confidence\":0.964646339416504},{\"character\":\".\",\"confidence\":0.8501640558242798},{\"character\":\"0\",\"confidence\":0.9917362332344056},{\"character\":\"0\",\"confidence\":0.9908128380775452}],\"itemstring\":\"找赎:0.00\"},{\"itemcoord\":{\"x\":188,\"y\":1276,\"width\":300,\"height\":50},\"words\":[{\"character\":\"结\",\"confidence\":0.9999120235443116},{\"character\":\"算\",\"confidence\":0.9998456239700316},{\"character\":\"方\",\"confidence\":0.9999324083328248},{\"character\":\"式\",\"confidence\":0.9999651908874512},{\"character\":\"金\",\"confidence\":0.9984872341156006},{\"character\":\"额\",\"confidence\":0.9947147965431212}],\"itemstring\":\"结算方式金额\"},{\"itemcoord\":{\"x\":191,\"y\":1346,\"width\":89,\"height\":49},\"words\":[{\"character\":\"微\",\"confidence\":0.9966641068458556},{\"character\":\"信\",\"confidence\":0.9999427795410156}],\"itemstring\":\"微信\"},{\"itemcoord\":{\"x\":399,\"y\":1352,\"width\":98,\"height\":40},\"words\":[{\"character\":\"1\",\"confidence\":0.9980598092079164},{\"character\":\"5\",\"confidence\":0.9994003772735596},{\"character\":\"4\",\"confidence\":0.999711573123932},{\"character\":\".\",\"confidence\":0.8834667801856995},{\"character\":\"5\",\"confidence\":0.9954633116722108}],\"itemstring\":\"154.5\"},{\"itemcoord\":{\"x\":186,\"y\":1404,\"width\":399,\"height\":39},\"words\":[{\"character\":\"会\",\"confidence\":0.9991254210472108},{\"character\":\"员\",\"confidence\":0.9999327659606934},{\"character\":\"卡\",\"confidence\":0.9893656969070436},{\"character\":\":\",\"confidence\":0.4973417818546295},{\"character\":\"3\",\"confidence\":0.7390124201774597},{\"character\":\"3\",\"confidence\":0.5891523957252502},{\"character\":\"5\",\"confidence\":0.4280991852283478},{\"character\":\"4\",\"confidence\":0.2590048015117645},{\"character\":\"1\",\"confidence\":0.4002929031848908},{\"character\":\"1\",\"confidence\":0.3576459288597107},{\"character\":\"8\",\"confidence\":0.995469570159912},{\"character\":\"8\",\"confidence\":0.9953532218933106},{\"character\":\"1\",\"confidence\":0.9982499480247498},{\"character\":\"1\",\"confidence\":0.9989087581634522}],\"itemstring\":\"会员卡:3354118811\"}],\"session_id\":\"12537637201924331043\"}}";
        ResultObject r = new ResultObject();
        ha.afterExecute(content, r);

        //System.out.println(r.toString());
    }
}
