'use strict'

import {videoSupport} from './ffmpeg-helper'
import VideoServer from './VideoServer'
const { app, BrowserWindow, Menu, ipcMain, globalShortcut } = require('electron')
const electron = require('electron')
const dialog = require('electron').dialog
const { autoUpdater } = require('electron-updater')
const log = require('electron-log')

/**
 * Set `__static` path to static files in production
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-static-assets.html
 */
if (process.env.NODE_ENV !== 'development') {
  log.transports.file.level = 'info'
  Object.assign(console, log.functions)
  global.__static = require('path').join(__dirname, '/static').replace(/\\/g, '\\\\')
}

let mainWindow
const winURL = process.env.NODE_ENV === 'development'
  ? `http://localhost:9080`
  : `file://${__dirname}/index.html`

// const INDEX_STREAM_HTML = 'src/renderer/index-stream.html'
let httpServer

function onVideoFileSeleted (videoFilePath) {
  videoSupport(videoFilePath).then((checkResult) => {
    if (checkResult.videoCodecSupport && checkResult.audioCodecSupport) {
      console.log('INDEX_HTML')
      if (!httpServer) {
        httpServer = new VideoServer()
      }
      httpServer.videoSourceInfo = { videoSourcePath: videoFilePath, checkResult: checkResult }
      httpServer.createServer()
      if (httpServer) {
        console.log('createVideoServer success')
        // mainWindow.webContents.send('openVideo', JSON.stringify({ duration: checkResult.duration, videoPath: videoFilePath }))
        mainWindow.webContents.send('openVideo', checkResult.duration, videoFilePath)
      }
    }
    if (!checkResult.videoCodecSupport || !checkResult.audioCodecSupport) {
      console.log('INDEX_HTML')
      if (!httpServer) {
        httpServer = new VideoServer()
      }
      httpServer.videoSourceInfo = { videoSourcePath: videoFilePath, checkResult: checkResult }
      httpServer.createServer()
      if (httpServer) {
        console.log('createVideoServer success')
        mainWindow.webContents.send('openVideo', checkResult.duration, videoFilePath)
      }
    }
  }).catch((err) => {
    console.log('video format error', err)
    const options = {
      type: 'info',
      title: 'Error',
      message: 'It is not a video file!' + err,
      buttons: ['OK']
    }
    dialog.showMessageBox(options, function (index) {
      console.log('showMessageBox', index)
    })
  })
}

function createWindow () {
  /**
   * Initial window options
   */
  mainWindow = new BrowserWindow({
    show: false,
    useContentSize: true,
    webPreferences: {
      nodeIntegration: true
    //   webSecurity: false
    },
    title: '',
    icon: `${__dirname}/../../icon-sm.ico`
  })
  mainWindow.maximize()
  mainWindow.show()

  mainWindow.loadURL(winURL, {
    extraHeaders: "Content-Security-Policy: default-src 'self'"
  })

  mainWindow.on('closed', () => {
    mainWindow = null
  })

  Menu.setApplicationMenu(null)
  ipcMain.on('fileDrop', (event, arg) => {
    console.log('fileDrop:', arg)
    onVideoFileSeleted(arg)
  })
  ipcMain.on('openVideo', (event, arg) => {
    electron.dialog.showOpenDialog({
      properties: ['openFile'],
      filters: [
        {name: 'Movies', extensions: ['mpg', 'wmv', 'avi', 'mp4', 'mts', 'mov']}
      ]
    }).then(result => {
      if (result && mainWindow && result.filePaths.length > 0) {
        onVideoFileSeleted(result.filePaths[0])
      }
    })
  })
}

const gotTheLock = app.requestSingleInstanceLock()

if (!gotTheLock) {
  app.quit()
} else {
  app.on('second-instance', (event, commandLine, workingDirectory) => {
    // 当运行第二个实例时,将会聚焦到myWindow这个窗口
    if (mainWindow) {
      if (mainWindow.isMinimized()) mainWindow.restore()
      mainWindow.focus()
    }
  })
}

app.on('ready', () => {
  createWindow()
  globalShortcut.register('CommandOrControl+Shift+I', () => {
    mainWindow.webContents.isDevToolsOpened()
      ? mainWindow.webContents.closeDevTools()
      : mainWindow.webContents.openDevTools()
  })

  autoUpdater.on('error', e => {
    console.log(e)
    electron.dialog.showErrorBox('版本更新', '从服务器获取版本更新出错，请稍后重试')
  })

  autoUpdater.on('update-downloaded', () => {
    autoUpdater.quitAndInstall()
  })

  autoUpdater.checkForUpdates()
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (mainWindow === null) {
    createWindow()
  }
})
