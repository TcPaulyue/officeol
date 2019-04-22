class OpsHandler{
    static ops2Message(deltaOps){
        let message = {
            operation:"",
            position:0,
            message:"",
            fileId:"",
            userId:"",
            state:0,
        }
        deltaOps.forEach(element => {
            if(element.hasOwnProperty('retain')){
                message['position'] = element['retain'];
            }
            else if(element.hasOwnProperty('insert')){
                message['operation'] = 'insert';
                message['message'] = element['insert'];
            }
            else if(element.hasOwnProperty('delete')){
                message['operation'] = 'delete';
                message['message'] = element['delete'].toString();
            }
        });
        return message;
    }
    static message2Ops(message){
        let ops = [];
        if(message['operation']==='noop'){
            return null;
        }
        if(message['position']!==0){
            ops.push({
                retain:message['position']
            });
        }
        if(message['operation']==='insert'){
            ops.push({
                insert:message['message']
            })
        }
        if(message['operation']==='delete'){
            ops.push({
                delete:parseInt(message['message'])
            })
        }
        return ops;
    }
    static opsToOpsList(deltaOps){
        let position = 0;
        let opsList = [];
        deltaOps.forEach((element)=>{
            if(element.hasOwnProperty('retain')){
                position = element['retain'];
            }
            if(element.hasOwnProperty('insert')){
                let newOps = [];
                newOps.push({retain:position});
                newOps.push(element);
                position = position + element['insert'].length;
                opsList.push(newOps);
            }
            else if(element.hasOwnProperty('delete')){
                let newOps = [];
                newOps.push({retain:position});
                newOps.push(element);
                opsList.push(newOps);
            }
        })
        return opsList;
    }
}
export default OpsHandler;