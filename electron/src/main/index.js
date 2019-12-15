'use strict'

import {videoSupport} from './ffmpeg-helper'
import VideoServer from './VideoServer'
const { app, BrowserWindow, Menu, ipcMain } = require('electron')
const electron = require('electron')
const dialog = require('electron').dialog

/**
 * Set `__static` path to static files in production
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-static-assets.html
 */
if (process.env.NODE_ENV !== 'development') {
  global.__static = require('path').join(__dirname, '/static').replace(/\\/g, '\\\\')
}

let mainWindow
const winURL = process.env.NODE_ENV === 'development'
  ? `http://localhost:9080`
  : `file://${__dirname}/index.html`

const INDEX_HTML = 'src/renderer/index.html'
// const INDEX_STREAM_HTML = 'src/renderer/index-stream.html'
let httpServer
let currentLoadFile

function onVideoFileSeleted (videoFilePath) {
  videoSupport(videoFilePath).then((checkResult) => {
    if (checkResult.videoCodecSupport && checkResult.audioCodecSupport) {
      console.log('INDEX_HTML')
      if (httpServer) {
        httpServer.killFfmpegCommand()
      }
      if (currentLoadFile === INDEX_HTML) {
        mainWindow.webContents.send('fileSelected', videoFilePath)
      } else {
        ipcMain.once('ipcRendererReady', (event, args) => {
          mainWindow.webContents.send('fileSelected', videoFilePath)
        })
        mainWindow.loadFile(INDEX_HTML)
        currentLoadFile = INDEX_HTML
      }
    }
    if (!checkResult.videoCodecSupport || !checkResult.audioCodecSupport) {
      console.log('INDEX_STREAM_HTML')
      if (!httpServer) {
        httpServer = new VideoServer()
      }
      httpServer.videoSourceInfo = { videoSourcePath: videoFilePath, checkResult: checkResult }
      httpServer.createServer()
      if (httpServer) {
        console.log('createVideoServer success')
        ipcMain.once('ipcRendererReady', (event, args) => {
          mainWindow.webContents.send('duration', checkResult.duration)
        })
        mainWindow.webContents.send('openVideo')
        // mainWindow.loadFile(INDEX_HTML)
        // currentLoadFile = INDEX_HTML
      }
    }
  }).catch((err) => {
    console.log('video format error', err)
    const options = {
      type: 'info',
      title: 'Error',
      message: 'It is not a video file!',
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
    height: 563,
    useContentSize: true,
    width: 1000
  })

  mainWindow.loadURL(winURL)

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
        {name: 'Movies', extensions: ['mkv', 'avi', 'mp4', 'mts', 'm2ts']}
      ]
    }, (result) => {
      console.log(result)

      if (result && mainWindow && result.length > 0) {
        onVideoFileSeleted(result[0])
      }
    })
  })
}

app.on('ready', createWindow)

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

/**
 * Auto Updater
 *
 * Uncomment the following code below and install `electron-updater` to
 * support auto updating. Code Signing with a valid certificate is required.
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-electron-builder.html#auto-updating
 */

/*
import { autoUpdater } from 'electron-updater'

autoUpdater.on('update-downloaded', () => {
  autoUpdater.quitAndInstall()
})

app.on('ready', () => {
  if (process.env.NODE_ENV === 'production') autoUpdater.checkForUpdates()
})
 */
