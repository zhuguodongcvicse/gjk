import { deepClone } from "@/util/util";
import request from '@/router/axios'

// 遍历硬件建模前部分数据，得到前部分机箱
export const traverseFrontHardwarelib = hardwarelibFrontData => {
    let caseList = []
    for (const i in hardwarelibFrontData) {
        for (const j in hardwarelibFrontData[i].datas) {
            if (hardwarelibFrontData[i].datas[j].json != null && hardwarelibFrontData[i].datas[j].json.properties != null && hardwarelibFrontData[i].datas[j].json.properties.caseName != null) {
                caseList = caseList.concat(hardwarelibFrontData[i].datas[j].json.properties)
                caseList = deepClone(caseList)
            }
        }
    }
    return caseList
}
//遍历硬件建模后部分数据，得到后部分机箱
export const traverseBackHardwarelib = hardwarelibBackData => {
    let caseList = []
    for (const i in hardwarelibBackData) {
        for (const j in hardwarelibBackData[i].datas) {
            if (hardwarelibBackData[i].datas[j].json != null && hardwarelibBackData[i].datas[j].json.properties != null && hardwarelibBackData[i].datas[j].json.properties.caseName != null) {
                caseList = caseList.concat(hardwarelibBackData[i].datas[j].json.properties)
                caseList = deepClone(caseList)
            }
        }
    }
    return caseList
}
//遍历前机箱，得到所有前板卡
export const getFrontBoardOfCaseList = frontCaseList => {
    let boardList = []
    for (const i in frontCaseList) {
        if (frontCaseList[i].frontBoardList != null) {
            for (const j in frontCaseList[i].frontBoardList) {
                boardList.push(frontCaseList[i].frontBoardList[j])
                boardList = deepClone(boardList)
            }
        }
    }
    return boardList
}
//遍历后机箱，得到所有后板卡
export const getBackBoardOfCaseList = backCaseList => {
    let boardList = []
    for (const i in backCaseList) {
        if (backCaseList[i].backBoardList != null) {
            for (const j in backCaseList[i].backBoardList) {
                boardList.push(backCaseList[i].backBoardList[j])
                boardList = deepClone(boardList)
            }
        }
    }
    return boardList
}
//遍历前板卡，得到所有芯片
export const getChipOfCaseList = frontBoardOfCaseList => {
    let chipList = []
    for (const i in frontBoardOfCaseList.board0List) {
        for (const j in frontBoardOfCaseList.board0List[i].chipList) {
            chipList.push(frontBoardOfCaseList.board0List[i].chipList[j])
            chipList = deepClone(chipList)
        }
    }
    for (const i in frontBoardOfCaseList.board1List) {
        for (const j in frontBoardOfCaseList.board1List[i].chipList) {
            chipList.push(frontBoardOfCaseList.board1List[i].chipList[j])
            chipList = deepClone(chipList)
        }
    }
    return chipList
}

export const createXmlEntityMap = paramsList => {
    paramsList[0] = JSON.parse(paramsList[0])
    paramsList[1] = JSON.parse(paramsList[1])
    /* for (const i in paramsList[0]) {
        paramsList[0][i] = JSON.parse(paramsList[0][i])
        paramsList[1][i] = JSON.parse(paramsList[1][i])
    } */
    var frontJson = paramsList[0]
    var backJson = paramsList[1]
    var linkRelation = JSON.parse(paramsList[2])
    // console.log("frontJson",frontJson)
    // console.log("backJson",backJson)
    // console.log("linkRelation",linkRelation)
    //前面机箱的数组
    var frontCaseList = traverseFrontHardwarelib(frontJson)
    //后面机箱的数组
    var backCaseList = traverseBackHardwarelib(backJson)
    // console.log("frontCaseList",frontCaseList)
    // console.log("backCaseList",backCaseList)
    //所有前板卡的数组
    var frontBoardOfCaseList
    if (frontCaseList.length != 0) {
        frontBoardOfCaseList = getFrontBoardOfCaseList(frontCaseList)
    }
    // console.log("frontBoardOfCaseList",frontBoardOfCaseList)
    //所有后板卡的数组
    var backBoardOfCaseList
    if (backCaseList.length != 0) {
        backBoardOfCaseList = getBackBoardOfCaseList(backCaseList)
    }
    // console.log("backBoardOfCaseList",backBoardOfCaseList)
    //所有cpu的数组
    var chipOfCaseList
    if (frontBoardOfCaseList.length != 0) {
        chipOfCaseList = getChipOfCaseList(frontBoardOfCaseList)
        // console.log("chipOfCaseList",chipOfCaseList)
    }
    //<hrConfig>
    let hrConfigXmlEntityMap = new Object()
    hrConfigXmlEntityMap.lableName = "hrConfig"
    hrConfigXmlEntityMap.attributeMap = {}
    hrConfigXmlEntityMap.xmlEntityMaps = []

    //<caseTotalNum>
    let caseTotalNumXmlEntityMap = new Object()
    caseTotalNumXmlEntityMap.lableName = "caseTotalNum"
    caseTotalNumXmlEntityMap.attributeMap = { name: frontCaseList.length }
    caseTotalNumXmlEntityMap.xmlEntityMaps = []
    //将<caseTotalNum>插入<hrConfig>
    hrConfigXmlEntityMap.xmlEntityMaps.push(caseTotalNumXmlEntityMap)
    for (const i in frontCaseList) {
        //<case>
        let caseXmlEntityMap = new Object()
        caseXmlEntityMap.lableName = "case"
        caseXmlEntityMap.attributeMap = { ID: frontCaseList[i].ID, bdNum: frontCaseList[i].bdNum, SN: frontCaseList[i].uniqueId }
        caseXmlEntityMap.xmlEntityMaps = []
        /* //<calculateBoard>
        let calculateBoardXmlEntityMap = new Object()
        calculateBoardXmlEntityMap.lableName = "calculateBoard"
        calculateBoardXmlEntityMap.attributeMap = {}
        calculateBoardXmlEntityMap.xmlEntityMaps = []
        //<FpgaBoard>
        let FpgaBoardXmlEntityMap = new Object()
        FpgaBoardXmlEntityMap.lableName = "FpgaBoard"
        FpgaBoardXmlEntityMap.attributeMap = {}
        FpgaBoardXmlEntityMap.xmlEntityMaps = []
        //<exchangeBoard>
        let exchangeBoardXmlEntityMap = new Object()
        exchangeBoardXmlEntityMap.lableName = "exchangeBoard"
        exchangeBoardXmlEntityMap.attributeMap = {}
        exchangeBoardXmlEntityMap.xmlEntityMaps = []
        //<interfaceBoard>
        let interfaceBoardXmlEntityMap = new Object()
        interfaceBoardXmlEntityMap.lableName = "interfaceBoard"
        interfaceBoardXmlEntityMap.attributeMap = {}
        interfaceBoardXmlEntityMap.xmlEntityMaps = [] */

        for (const j in frontBoardOfCaseList) {
            if (frontBoardOfCaseList[j].uniqueId.indexOf(frontCaseList[i].uniqueId) != -1) {
                if (frontBoardOfCaseList[j].boardType == 0) {
                    //<board SN="2" ID="0" cpuNum="1" type="0">
                    let boardXmlEntityMap = new Object()
                    boardXmlEntityMap.lableName = "board"
                    boardXmlEntityMap.attributeMap = {
                        ID: frontBoardOfCaseList[j].ID,
                        cpuNum: frontBoardOfCaseList[j].chipList.length,
                        type: frontBoardOfCaseList[j].boardType,
                        SN: frontBoardOfCaseList[j].uniqueId
                    }
                    boardXmlEntityMap.xmlEntityMaps = []
                    //<ExchangeCpu>
                    let ExchangeCpuXmlEntityMap = new Object()
                    ExchangeCpuXmlEntityMap.lableName = "ExchangeCpu"
                    ExchangeCpuXmlEntityMap.attributeMap = {}
                    ExchangeCpuXmlEntityMap.xmlEntityMaps = []

                    //<ExternalInf>
                    let ExternalInfXmlEntityMap = new Object()
                    ExternalInfXmlEntityMap.lableName = "ExternalInf"
                    ExternalInfXmlEntityMap.attributeMap = {}
                    ExternalInfXmlEntityMap.xmlEntityMaps = []

                    if (frontBoardOfCaseList[j].ExchangeCpu.length != 0) {
                        for (const k in frontBoardOfCaseList[j].ExchangeCpu) {
                            //<link>
                            let linkXmlEntityMap = new Object()
                            linkXmlEntityMap.lableName = "link"
                            linkXmlEntityMap.attributeMap = { linkType: frontBoardOfCaseList[j].ExchangeCpu[k][1] }
                            linkXmlEntityMap.xmlEntityMaps = []

                            //<inf>
                            let infXmlEntityMap = new Object()
                            infXmlEntityMap.lableName = "inf"
                            infXmlEntityMap.attributeMap = {
                                ID: frontBoardOfCaseList[j].ExchangeCpu[k][0].ID,
                                infRate: frontBoardOfCaseList[j].ExchangeCpu[k][0].infRate,
                                infName: frontBoardOfCaseList[j].ExchangeCpu[k][0].infName,
                                ioType: frontBoardOfCaseList[j].ExchangeCpu[k][2],
                                SN: frontBoardOfCaseList[j].ExchangeCpu[k][0].uniqueId
                            }
                            infXmlEntityMap.xmlEntityMaps = []
                            //将<inf>插入<link>
                            linkXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                            //将<link>插入<ExternalInf>
                            ExternalInfXmlEntityMap.xmlEntityMaps.push(linkXmlEntityMap)
                        }
                    }
                    //将<ExternalInf>插入<ExchangeCpu>
                    ExchangeCpuXmlEntityMap.xmlEntityMaps.push(ExternalInfXmlEntityMap)
                    //将<ExchangeCpu>插入<board>
                    boardXmlEntityMap.xmlEntityMaps.push(ExchangeCpuXmlEntityMap)

                    for (const k in frontBoardOfCaseList[j].chipList) {
                        // console.log("frontBoardOfCaseList[j].chipList",frontBoardOfCaseList[j].chipList[k])
                        if (frontBoardOfCaseList[j].chipList.length != 0) {
                            if (frontBoardOfCaseList[j].chipList[k].IP != null) {
                                var ip = frontBoardOfCaseList[j].chipList[k].IP
                            } else {
                                var ip = '此IP没有配置'
                            }
                            //<cpu>
                            let cpuXmlEntityMap = new Object()
                            cpuXmlEntityMap.lableName = "cpu"
                            cpuXmlEntityMap.attributeMap = {
                                ID: frontBoardOfCaseList[j].chipList[k].ID,
                                name: frontBoardOfCaseList[j].chipList[k].chipName,
                                nodeID: frontBoardOfCaseList[j].chipList[k].nodeID,
                                ipConfig: ip,
                                hrTypeName: frontBoardOfCaseList[j].chipList[k].hrTypeName,
                                RecvRate: frontBoardOfCaseList[j].chipList[k].recvRate,
                                coreNum: frontBoardOfCaseList[j].chipList[k].coreNum,
                                memSize: frontBoardOfCaseList[j].chipList[k].memSize,
                                SN: frontBoardOfCaseList[j].chipList[k].uniqueId
                            }
                            cpuXmlEntityMap.xmlEntityMaps = []
                            if (frontBoardOfCaseList[j].chipList[k].infOfChipList.length != 0) {
                                for (const m in frontBoardOfCaseList[j].chipList[k].infOfChipList) {
                                    //<inf>
                                    let infXmlEntityMap = new Object()
                                    infXmlEntityMap.lableName = "inf"
                                    infXmlEntityMap.attributeMap = {
                                        ID: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].ID,
                                        infRate: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].infRate,
                                        infName: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].infName,
                                        ioType: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].ioType,
                                        SN: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].uniqueId
                                    }
                                    infXmlEntityMap.xmlEntityMaps = []
                                    //将<inf>插入<cpu>
                                    cpuXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                                }
                            }
                            //将<cpu>插入<board>
                            boardXmlEntityMap.xmlEntityMaps.push(cpuXmlEntityMap)
                        }
                    }
                    /* //将<board>插入<calculateBoard>
                    calculateBoardXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap) */
                    //将<board>插入<case>
                    caseXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap)
                }
                if (frontBoardOfCaseList[j].boardType == 1) {
                    //<board SN="75247" ID="" cpuNum="1" type="3">
                    let boardXmlEntityMap = new Object()
                    boardXmlEntityMap.lableName = "board"
                    boardXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].ID, cpuNum: frontBoardOfCaseList[j].chipList.length, type: frontBoardOfCaseList[j].boardType, SN: frontBoardOfCaseList[j].uniqueId }
                    boardXmlEntityMap.xmlEntityMaps = []
                    //<InfLink>
                    let InfLinkXmlEntityMap = new Object()
                    InfLinkXmlEntityMap.lableName = "InfLink"
                    InfLinkXmlEntityMap.attributeMap = {}
                    InfLinkXmlEntityMap.xmlEntityMaps = []
                    //<InternalLink>
                    let InternalLinkXmlEntityMap = new Object()
                    InternalLinkXmlEntityMap.lableName = "InternalLink"
                    InternalLinkXmlEntityMap.attributeMap = {}
                    InternalLinkXmlEntityMap.xmlEntityMaps = []
                    //<ExternalLink>
                    let ExternalLinkXmlEntityMap = new Object()
                    ExternalLinkXmlEntityMap.lableName = "ExternalLink"
                    ExternalLinkXmlEntityMap.attributeMap = {}
                    ExternalLinkXmlEntityMap.xmlEntityMaps = []

                    if (frontBoardOfCaseList[j].InternalLink != null && frontBoardOfCaseList[j].InternalLink.length != 0) {
                        for (const k in frontBoardOfCaseList[j].InternalLink) {
                            //<link>
                            let linkXmlEntityMap = new Object()
                            linkXmlEntityMap.lableName = "link"
                            linkXmlEntityMap.attributeMap = {}
                            linkXmlEntityMap.xmlEntityMaps = []
                            //<startCpu>
                            let startCpuXmlEntityMap = new Object()
                            startCpuXmlEntityMap.lableName = "startCpu"
                            startCpuXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].InternalLink[k][0].ID }
                            startCpuXmlEntityMap.xmlEntityMaps = []

                            //<inf>
                            let infXmlEntityMap1 = new Object()
                            infXmlEntityMap1.lableName = "inf"
                            infXmlEntityMap1.attributeMap = { ID: frontBoardOfCaseList[j].InternalLink[k][1].ID }
                            infXmlEntityMap1.xmlEntityMaps = []
                            //将<inf>插入<startCpu>
                            startCpuXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap1)
                            //将<startCpu>插入<link>
                            linkXmlEntityMap.xmlEntityMaps.push(startCpuXmlEntityMap)
                            //<endCpu>
                            let endCpuXmlEntityMap = new Object()
                            endCpuXmlEntityMap.lableName = "endCpu"
                            endCpuXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].InternalLink[k][2].ID }
                            endCpuXmlEntityMap.xmlEntityMaps = []

                            //<inf>
                            let infXmlEntityMap2 = new Object()
                            infXmlEntityMap2.lableName = "inf"
                            infXmlEntityMap2.attributeMap = { ID: frontBoardOfCaseList[j].InternalLink[k][3].ID }
                            infXmlEntityMap2.xmlEntityMaps = []
                            //将<inf>插入<startCpu>
                            endCpuXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap2)
                            //将<startCpu>插入<link>
                            linkXmlEntityMap.xmlEntityMaps.push(endCpuXmlEntityMap)
                            //将<link>插入<InternalLink>
                            InternalLinkXmlEntityMap.xmlEntityMaps.push(linkXmlEntityMap)
                        }
                    }
                    //将<InternalLink>插入<InfLink>
                    InfLinkXmlEntityMap.xmlEntityMaps.push(InternalLinkXmlEntityMap)

                    if (frontBoardOfCaseList[j].outLinkArr != null && frontBoardOfCaseList[j].outLinkArr.length != 0) {
                        for (const k in frontBoardOfCaseList[j].outLinkArr) {
                            //<Cpu>
                            let CpuXmlEntityMap = new Object()
                            CpuXmlEntityMap.lableName = "Cpu"
                            CpuXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].outLinkArr[k][0].ID }
                            CpuXmlEntityMap.xmlEntityMaps = []

                            //<inf>
                            let infXmlEntityMap = new Object()
                            infXmlEntityMap.lableName = "inf"
                            infXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].outLinkArr[k][1].ID }
                            infXmlEntityMap.xmlEntityMaps = []

                            //<linkType>
                            let linkTypeXmlEntityMap = new Object()
                            linkTypeXmlEntityMap.lableName = "linkType"
                            let linkTypeValue = frontBoardOfCaseList[j].outLinkArr[k][2]
                            if (linkTypeValue == '1') {
                                linkTypeXmlEntityMap.attributeMap = { value: linkTypeValue, ID: frontBoardOfCaseList[j].outLinkArr[k][3].ID }
                            } else if (linkTypeValue == '0') {
                                linkTypeXmlEntityMap.attributeMap = { value: linkTypeValue }
                            }
                            linkTypeXmlEntityMap.xmlEntityMaps = []
                            //将<linkType>插入<inf>
                            infXmlEntityMap.xmlEntityMaps.push(linkTypeXmlEntityMap)
                            //将<inf>插入<Cpu>
                            CpuXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                            //将<Cpu>插入<ExternalLink>
                            ExternalLinkXmlEntityMap.xmlEntityMaps.push(CpuXmlEntityMap)
                        }
                        //将<ExternalLink>插入<InfLink>
                        InfLinkXmlEntityMap.xmlEntityMaps.push(ExternalLinkXmlEntityMap)
                    }
                    //将<InfLink>插入<board>
                    boardXmlEntityMap.xmlEntityMaps.push(InfLinkXmlEntityMap)

                    if (frontBoardOfCaseList[j].chipList.length != 0) {
                        for (const k in frontBoardOfCaseList[j].chipList) {
                            if (frontBoardOfCaseList[j].chipList[k].IP != null) {
                                var ip = frontBoardOfCaseList[j].chipList[k].IP
                            } else {
                                var ip = '此IP没有配置'
                            }
                            //<cpu>
                            let cpuXmlEntityMap = new Object()
                            cpuXmlEntityMap.lableName = "cpu"
                            cpuXmlEntityMap.attributeMap = {
                                ID: frontBoardOfCaseList[j].chipList[k].ID,
                                nodeID: frontBoardOfCaseList[j].chipList[k].nodeID,
                                name: frontBoardOfCaseList[j].chipList[k].chipName,
                                ipConfig: ip,
                                hrTypeName: frontBoardOfCaseList[j].chipList[k].hrTypeName,
                                RecvRate: frontBoardOfCaseList[j].chipList[k].recvRate,
                                coreNum: frontBoardOfCaseList[j].chipList[k].coreNum,
                                memSize: frontBoardOfCaseList[j].chipList[k].memSize,
                                SN: frontBoardOfCaseList[j].chipList[k].uniqueId
                            }
                            cpuXmlEntityMap.xmlEntityMaps = []
                            if (frontBoardOfCaseList[j].chipList[k].infOfChipList.length != 0) {
                                for (const m in frontBoardOfCaseList[j].chipList[k].infOfChipList) {
                                    //<inf>
                                    let infXmlEntityMap = new Object()
                                    infXmlEntityMap.lableName = "inf"
                                    infXmlEntityMap.attributeMap = {
                                        ID: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].ID,
                                        infRate: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].infRate,
                                        infName: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].infName,
                                        ioType: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].ioType,
                                        SN: frontBoardOfCaseList[j].chipList[k].infOfChipList[m].uniqueId
                                    }
                                    infXmlEntityMap.xmlEntityMaps = []
                                    //将<inf>插入<cpu>
                                    cpuXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                                }
                            }
                            //将<cpu>插入<board>
                            boardXmlEntityMap.xmlEntityMaps.push(cpuXmlEntityMap)
                        }
                    }

                    /* //将<board>插入<FpgaBoard>
                    FpgaBoardXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap) */
                    //将<board>插入<case>
                    caseXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap)
                }
                if (frontBoardOfCaseList[j].boardType == 2) {
                    //<board>
                    let boardXmlEntityMap = new Object()
                    boardXmlEntityMap.lableName = "board"
                    boardXmlEntityMap.attributeMap = { ID: frontBoardOfCaseList[j].ID, type: frontBoardOfCaseList[j].boardType, SN: frontBoardOfCaseList[j].uniqueId }
                    boardXmlEntityMap.xmlEntityMaps = []
                    //<inf>
                    if (frontBoardOfCaseList[j].backBoardInfList.length != 0) {
                        for (const k in frontBoardOfCaseList[j].backBoardInfList) {
                            let infXmlEntityMap = new Object()
                            infXmlEntityMap.lableName = "inf"
                            infXmlEntityMap.attributeMap = {
                                ID: frontBoardOfCaseList[j].backBoardInfList[k].ID,
                                infRate: frontBoardOfCaseList[j].backBoardInfList[k].infRate,
                                infName: frontBoardOfCaseList[j].backBoardInfList[k].infName,
                                ioType: frontBoardOfCaseList[j].backBoardInfList[k].ioType,
                                SN: frontBoardOfCaseList[j].backBoardInfList[k].uniqueId
                            }
                            infXmlEntityMap.xmlEntityMaps = []
                            //将<inf>插入<board>
                            boardXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                        }
                    }
                    /* //将<board>插入<exchangeBoard>
                    exchangeBoardXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap) */
                    //将<board>插入<case>
                    caseXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap)
                }
            }
        }
        /* //将<calculateBoard>插入<case>
        caseXmlEntityMap.xmlEntityMaps.push(calculateBoardXmlEntityMap)
        //将<FpgaBoard>插入<case>
        caseXmlEntityMap.xmlEntityMaps.push(FpgaBoardXmlEntityMap)
        //将<exchangeBoard>插入<case>
        caseXmlEntityMap.xmlEntityMaps.push(exchangeBoardXmlEntityMap) */

        for (const j in backBoardOfCaseList) {
            if (backBoardOfCaseList[j].uniqueId.indexOf(frontCaseList[i].uniqueId) != -1) {

                //<board>
                let boardXmlEntityMap = new Object()
                boardXmlEntityMap.lableName = "board"
                boardXmlEntityMap.attributeMap = { ID: backBoardOfCaseList[j].ID, type: backBoardOfCaseList[j].boardType, SN: backBoardOfCaseList[j].uniqueId }
                boardXmlEntityMap.xmlEntityMaps = []

                if (backBoardOfCaseList[j].backBoardInfList != null) {
                    for (const k in backBoardOfCaseList[j].backBoardInfList) {
                        //<inf>
                        let infXmlEntityMap = new Object()
                        infXmlEntityMap.lableName = "inf"
                        infXmlEntityMap.attributeMap = {
                            ID: backBoardOfCaseList[j].backBoardInfList[k].ID,
                            infRate: backBoardOfCaseList[j].backBoardInfList[k].infRate,
                            infName: backBoardOfCaseList[j].backBoardInfList[k].infName,
                            ioType: backBoardOfCaseList[j].backBoardInfList[k].ioType,
                            SN: backBoardOfCaseList[j].backBoardInfList[k].uniqueId
                        }
                        infXmlEntityMap.xmlEntityMaps = []
                        //将<inf>插入<board>
                        boardXmlEntityMap.xmlEntityMaps.push(infXmlEntityMap)
                    }
                }
                /* //将<board>插入<interfaceBoard>
                interfaceBoardXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap) */
                //将<board>插入<case>
                caseXmlEntityMap.xmlEntityMaps.push(boardXmlEntityMap)
            }
        }
        /* //将<interfaceBoard>插入<case>
        caseXmlEntityMap.xmlEntityMaps.push(interfaceBoardXmlEntityMap) */
        //将<case>插入<hrConfig>
        hrConfigXmlEntityMap.xmlEntityMaps.push(caseXmlEntityMap)
    }
    for (const j in linkRelation) {
        //<connection>
        let connectionXmlEntityMap = new Object()
        connectionXmlEntityMap.lableName = "connection"
        connectionXmlEntityMap.attributeMap = {}
        connectionXmlEntityMap.xmlEntityMaps = []
        let boardID0
        let boardID1
        for (const k in backBoardOfCaseList) {
            if (linkRelation[j][0].uniqueId.indexOf(backBoardOfCaseList[k].uniqueId) != -1) {
                boardID0 = backBoardOfCaseList[k].ID
            }
            if (linkRelation[j][1].uniqueId.indexOf(backBoardOfCaseList[k].uniqueId) != -1) {
                boardID1 = backBoardOfCaseList[k].ID
            }
        }
        let caseID0
        let caseID1
        // console.log("backCaseList",backCaseList)
        for (const k in backCaseList) {
            if (linkRelation[j][0].uniqueId.indexOf(backCaseList[k].uniqueId) != -1) {
                caseID0 = backCaseList[k].ID
                // console.log("caseID0++",caseID0)
            }
            if (linkRelation[j][1].uniqueId.indexOf(backCaseList[k].uniqueId) != -1) {
                caseID1 = backCaseList[k].ID
            }
        }
        // console.log("caseID0",caseID0)
        //<inf>
        let inf1XmlEntityMap0 = new Object()
        inf1XmlEntityMap0.lableName = "inf"
        inf1XmlEntityMap0.attributeMap = { ID: linkRelation[j][0].ID, boardID: boardID0, caseID: caseID0 }
        inf1XmlEntityMap0.xmlEntityMaps = []

        //<inf>
        let inf1XmlEntityMap1 = new Object()
        inf1XmlEntityMap1.lableName = "inf"
        inf1XmlEntityMap1.attributeMap = { ID: linkRelation[j][1].ID, boardID: boardID1, caseID: caseID1 }
        inf1XmlEntityMap1.xmlEntityMaps = []
        //将<inf>插入<connection>
        connectionXmlEntityMap.xmlEntityMaps.push(inf1XmlEntityMap0)
        connectionXmlEntityMap.xmlEntityMaps.push(inf1XmlEntityMap1)
        //将<connection>插入<hrConfig>
        hrConfigXmlEntityMap.xmlEntityMaps.push(connectionXmlEntityMap)
    }

    hrConfigXmlEntityMap.lableName = 'hrConfig'
    hrConfigXmlEntityMap.attributeMap = {}
    // console.log("hrConfigXmlEntityMap",hrConfigXmlEntityMap)
    
    return hrConfigXmlEntityMap
}