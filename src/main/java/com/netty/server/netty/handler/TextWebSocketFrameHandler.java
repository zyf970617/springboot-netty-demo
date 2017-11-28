package com.netty.server.netty.handler;

import com.netty.server.netty.util.RandomName;
import com.netty.server.netty.util.RedisDao;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by xsw on 2017/10/12.
 */
@Component
@Qualifier("textWebSocketFrameHandler")
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private RedisDao redisDao;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
        Channel incoming = ctx.channel();
        String uName = redisDao.getString(incoming.id()+"");
        for (Channel channel : channels) {
            if (channel != incoming){
                channel.writeAndFlush(new TextWebSocketFrame("[" + uName + "]" + msg.text()));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        String uName = new RandomName().getRandomName();

        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame("[新用户] - " + uName + " 加入"));
        }
        redisDao.saveString(incoming.id()+"",uName);
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        String uName = redisDao.getString(String.valueOf(incoming.id()));
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame("[用户] - " + uName + " 离开"));
        }
        redisDao.deleteString(String.valueOf(incoming.id()));

        //redisDao.saveString("cacheName",redisDao.getString("cacheName").replaceAll(uName,""));

        channels.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("用户:"+redisDao.getString(incoming.id()+"")+"在线");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("用户:"+redisDao.getString(incoming.id()+"")+"掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("用户:"+redisDao.getString(incoming.id()+"")+"异常");
        cause.printStackTrace();
        ctx.close();
    }

}