package com.netty.server.netty.Controller;

import com.netty.server.netty.handler.TextWebSocketFrameHandler;
import com.netty.server.netty.inter.RedisDao;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xsw on 2017/10/12.
 */
@Controller
public class URLController {

    @Autowired
    private TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Autowired
    private RedisDao redisDao;

    @RequestMapping("/")
    public String WebsocketChatClient(){
        return "/WebsocketChatClient";
    }


}
