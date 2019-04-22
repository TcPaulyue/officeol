import OpsHandler from './OpsHandler';
//消息队列类，处理消息队列的收发正确性
class MessageHandler{
    constructor(webSocket,userId,fileId){
        this.outgoing = {};
        this.userId = userId;
        this.fileId = fileId;
        this.myMsgs = 0;
        this.otherMsgs = 0;
        this.webSocket = webSocket;
        this.receiveBuffer = [];
        this.xform = this.xform.bind(this);
        this.generate = this.generate.bind(this);
        this.receive = this.receive.bind(this);
        this.receiveToBuffer = this.receiveToBuffer.bind(this);
        this.applyBuffer = this.applyBuffer.bind(this);
    }
    generate(deltaOps){
        //apply op locally
        if(deltaOps.length===0)
            return;
        else if(deltaOps.length===3){
            let opsList = OpsHandler.opsToOpsList(deltaOps);
            opsList.forEach((deltaOps)=>{
                this.generate(deltaOps);
            })
            return;
        }
        //sendToServer

       
        let message = OpsHandler.ops2Message(deltaOps);
        console.log(message);
        message['fileId'] = this.fileId;
        message['userId'] = this.userId;
        message['state'] = this.otherMsgs;
        if(this.webSocket.readyState===this.webSocket.OPEN){
            this.webSocket.send(JSON.stringify(message));
        }
        //add to outgoing
        message['state'] = this.myMsgs;
        this.outgoing[this.myMsgs] = message;

        this.myMsgs = this.myMsgs + 1;
    }
    receiveToBuffer(message){
        this.receiveBuffer.push(message);
    }
    applyBuffer(){
        let deltaOpsList = [];
        this.receiveBuffer.forEach((message)=>{
            deltaOpsList.push(this.receive(message));
        })
        this.receiveBuffer = [];
        return deltaOpsList;
    }
    receive(message){
        Object.keys(this.outgoing).forEach(element=>{
            if(this.outgoing[element]['state']<message['state']){
                delete this.outgoing[element];
            }
        });
        let msg = message;
        Object.keys(this.outgoing).forEach(element=>{
            msg = this.xform(msg,this.outgoing[element]);
        });
        let deltaOps = OpsHandler.message2Ops(message);
        this.otherMsgs = this.otherMsgs + 1;
        return deltaOps;
        
    }
    xform(msg1,msg2){
        if(msg1['operation']==='delete'&&msg2['operation']==='delete'){
            if(msg1['position'] > msg2['position']){
                msg1['position'] = msg1['position'] - parseInt(msg2['message']);
            }
            else if(msg1['position']<msg2['position']){
                msg2['position'] = msg2['position'] - parseInt(msg1['message']);
            }
            else{
                msg1['operation'] = 'noop';
                msg2['operation'] = 'noop';
                return 1;
            }
        }
        else if(msg1['operation']==='delete'&&msg2['operation']==='insert'){
            if(msg1['position'] >= msg2['position']){
                msg1['position'] = msg1['position'] + msg2['message'].length;
            }
            else{
                msg2['position'] = msg2['position'] - parseInt(msg1['message']);
            }
        }
        else if(msg1['operation']==='insert'&&msg2['operation']==='delete'){
            if(msg1['position']>msg2['position']){
                msg1['position'] = msg1['position'] - parseInt(msg2['message']);
            }
            else{
                msg2['position'] = msg2['position'] + msg1['message'].length;
            }
        }
        else if(msg1['operation']==='insert'&&msg2['operation']==='insert'){
            if(msg1['position']>msg2['position']){
                msg1['position'] = msg1['position'] + msg2['message'].length;
            }
            else if(msg1['position'] < msg2['position']){
                msg2['position'] = msg2['position'] + msg1['message'].length;
            }
            else{
                msg1['position'] = msg1['position'] + msg2['message'].length;
            }
        }
        else{
            console.log("unknow operation");
        }
        return msg1;
    }
}
export default MessageHandler;
