export interface AppConfig {
  serverUrl: string
  wsUrl: string
  appTitle: string
}

const defaultConfig: AppConfig = {
  serverUrl: import.meta.env.VITE_API_URL || '/api',
  wsUrl: import.meta.env.VITE_WS_URL || 'localhost:8080',
  appTitle: import.meta.env.VITE_APP_TITLE || 'hotel-xunni',
}

let config: AppConfig = { ...defaultConfig }

export const loadConfig = async (): Promise<AppConfig> => {
  try {
    const response = await fetch('/config.json')
    if (response.ok) {
      const remoteConfig = await response.json()
      config = { ...defaultConfig, ...remoteConfig }
    }
  } catch (error) {
    console.warn('Failed to load config.json, using default configuration')
  }
  return config
}

export const getConfig = (): AppConfig => {
  return config
}

export const getServerUrl = (): string => {
  return config.serverUrl
}

export const getWsUrl = (): string => {
  return config.wsUrl
}

export const getAppTitle = (): string => {
  return config.appTitle
}
